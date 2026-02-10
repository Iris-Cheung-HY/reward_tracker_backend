package com.rewardtracker.backend.service;

import com.rewardtracker.backend.model.*;
import com.rewardtracker.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RewardsDTOService {
    private final TransactionRecordsRepository transactionRecordsRepository;
    private final BankCardRewardsRepository bankCardRewardsRepository;
    private final UserCreditCardRepository userCreditCardRepository;

    public RewardsDTOService(
        BankCardRewardsRepository bankCardRewardsRepository, 
        TransactionRecordsRepository transactionRecordsRepository,
        UserCreditCardRepository userCreditCardRepository) {
        this.bankCardRewardsRepository = bankCardRewardsRepository;
        this.transactionRecordsRepository = transactionRecordsRepository;
        this.userCreditCardRepository = userCreditCardRepository;
    }

    @Transactional(readOnly = true)
    public List<RewardsDTO> getCalculatedRewards(Long userCardId) {
        UserCreditCard userCard = userCreditCardRepository.findById(userCardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        Long bankCreditCardId = userCard.getBankCreditCard().getId();
        
        List<BankCardRewards> rules = bankCardRewardsRepository.findByBankCreditCard_Id(bankCreditCardId);
        List<TransactionRecords> allTransactions = transactionRecordsRepository.findByUserCreditCardId(userCardId);

        LocalDate now = LocalDate.now();
        Month openMonth = userCard.getOpenMonth() != null ? userCard.getOpenMonth() : Month.JANUARY;

        Map<String, List<BankCardRewards>> groupMap = rules.stream()
            .filter(r -> extractGroupName(r.getConditions()) != null)
            .collect(Collectors.groupingBy(r -> extractGroupName(r.getConditions())));

        Map<Long, Double> rawSpendMap = new HashMap<>();
        for (BankCardRewards rule : rules) {
            double raw = getRawSpendForRule(rule, allTransactions, groupMap, openMonth, now);
            rawSpendMap.put(rule.getId(), raw);
        }

        double totalSpendInWindow = calculateWindowedAmount(allTransactions, new BankCardRewards(), openMonth, now);

        return rules.stream().map(rule -> {
            RewardsDTO dto = new RewardsDTO();
            dto.setMerchantType(rule.getMerchantType());
            dto.setRewardRate(rule.getRewardRate());
            dto.setType(rule.getType());
            dto.setConditions(rule.getConditions());

            double target = calculateTarget(rule, now);
            double used = 0.0;

            if ("TIME".equalsIgnoreCase(rule.getCalculationType())) {
                used = 1.0; 
                dto.setTotalAmount(null);
                dto.setEligible(true);
            } 
            else {
                if ("CREDIT".equalsIgnoreCase(rule.getType())) {
                    double rawSpend = rawSpendMap.getOrDefault(rule.getId(), 0.0);
                    used = Math.min(rawSpend, target); 
                    dto.setTotalAmount(target);
                    dto.setEligible(used >= target);
                } 
                else if ("MILESTONE".equalsIgnoreCase(rule.getType())) {
                    used = rawSpendMap.getOrDefault(rule.getId(), 0.0);
                    dto.setTotalAmount(target);
                    dto.setEligible(used >= target);
                }
                else if ("OTHERS".equalsIgnoreCase(rule.getMerchantType())) {
                    double deductions = 0.0;
                    for (BankCardRewards r : rules) {
                        if ("OTHERS".equalsIgnoreCase(r.getMerchantType())) continue;
                        double val = rawSpendMap.getOrDefault(r.getId(), 0.0);
                        if ("CREDIT".equalsIgnoreCase(r.getType())) {
                            deductions += Math.min(val, calculateTarget(r, now));
                        } else if ("POINTS".equalsIgnoreCase(r.getType())) {
                            deductions += val;
                        }
                    }
                    double finalBase = Math.max(0, totalSpendInWindow - deductions);
                    used = (rule.getRewardRate() != null) ? 
                           Math.round(finalBase * rule.getRewardRate() * 100.0) / 100.0 : 0.0;
                    dto.setTotalAmount(null);
                    dto.setEligible(true);
                }
                else {
                    used = rawSpendMap.getOrDefault(rule.getId(), 0.0);
                    if ("POINTS".equalsIgnoreCase(rule.getType())) {
                        used = (rule.getRewardRate() != null) ? 
                               Math.round(used * rule.getRewardRate() * 100.0) / 100.0 : used;
                    }
                    dto.setTotalAmount(target > 0 ? target : null);
                    dto.setEligible(target > 0 ? used >= target : true);
                }
            }

            dto.setUsedAmount(used);
            dto.setRemainingAmount(dto.getTotalAmount() != null ? Math.max(0, dto.getTotalAmount() - used) : 0.0);
            dto.setNextDueDate(calculateNextResetDate(rule, userCard, now));
            dto.setTotalPeriods(rule.getPeriods());

            return dto;
        }).collect(Collectors.toList());
    }

    private double getRawSpendForRule(BankCardRewards rule, List<TransactionRecords> txns, 
                                     Map<String, List<BankCardRewards>> groupMap, 
                                     Month openMonth, LocalDate now) {
        
        if ("MILESTONE".equalsIgnoreCase(rule.getType()) || "OTHERS".equalsIgnoreCase(rule.getMerchantType())) {
            return calculateWindowedAmount(txns, rule, openMonth, now);
        }

        String groupName = extractGroupName(rule.getConditions());
        List<TransactionRecords> eligibleTxns;

        if (groupName != null && groupMap.containsKey(groupName)) {
            List<BankCardRewards> groupRules = groupMap.get(groupName);
            eligibleTxns = txns.stream()
                .filter(t -> groupRules.stream().anyMatch(r -> r.isEligible(t.getMerchantType())))
                .collect(Collectors.toList());
        } else { 
            eligibleTxns = txns.stream()
                .filter(t -> rule.isEligible(t.getMerchantType()))
                .collect(Collectors.toList());
        }
        return calculateWindowedAmount(eligibleTxns, rule, openMonth, now);
    }

    private String extractGroupName(String conditions) {
        if (conditions != null && conditions.contains("GROUP_")) {
            int start = conditions.indexOf("GROUP_");
            int end = conditions.indexOf(":", start);
            return end != -1 ? conditions.substring(start, end) : conditions.substring(start);
        }
        return null;
    }

    private double calculateWindowedAmount(List<TransactionRecords> txns, BankCardRewards rule, Month openMonth, LocalDate now) {
        String freq = (rule.getFrequency() != null) ? rule.getFrequency().toUpperCase() : "";
        if (txns == null || txns.isEmpty()) return 0.0;

        return txns.stream().filter(t -> {
            LocalDate d = t.getDate();
            if (d == null) return false;

            switch (freq) {
                case "MONTHLY":
                    return d.getMonth() == now.getMonth() && d.getYear() == now.getYear();
                case "QUARTERLY":
                    return (d.getMonthValue() - 1) / 3 == (now.getMonthValue() - 1) / 3 && d.getYear() == now.getYear();
                case "SEMI_ANNUAL":
                    return (now.getMonthValue() <= 6 == d.getMonthValue() <= 6) && d.getYear() == now.getYear();
                case "ANNUAL":
                case "CALENDAR_YEAR":
                    return d.getYear() == now.getYear();
                case "ANNIVERSARY_YEAR":
                    LocalDate start = calculateAnniversaryStart(openMonth, now);
                    return !d.isBefore(start) && d.isBefore(start.plusYears(1));
                default:
                    return true;
            }
        }).mapToDouble(TransactionRecords::getAmount).sum();
    }

    private double calculateTarget(BankCardRewards rule, LocalDate now) {
        if (rule == null) return 0.0;
        if ("MONTHLY".equalsIgnoreCase(rule.getFrequency()) && now.getMonth() == Month.DECEMBER) {
            if (rule.getDecemberAmount() != null) return rule.getDecemberAmount();
        }
        if (rule.getPerPeriodAmount() != null) return rule.getPerPeriodAmount();
        return rule.getTotalAmount() != null ? rule.getTotalAmount() : 0.0;
    }

    private LocalDate calculateNextResetDate(BankCardRewards rule, UserCreditCard userCard, LocalDate now) {
        String freq = (rule.getFrequency() != null) ? rule.getFrequency().toUpperCase() : "";
        switch (freq) {
            case "MONTHLY":
                return now.with(TemporalAdjusters.lastDayOfMonth());
            case "QUARTERLY":
                int lastMonthOfQuarter = ((now.getMonthValue() - 1) / 3) * 3 + 3;
                return LocalDate.of(now.getYear(), lastMonthOfQuarter, 1).with(TemporalAdjusters.lastDayOfMonth());
            case "SEMI_ANNUAL":
                return now.getMonthValue() <= 6 ? LocalDate.of(now.getYear(), 6, 30) : LocalDate.of(now.getYear(), 12, 31);
            case "ANNUAL":
            case "CALENDAR_YEAR":
                return LocalDate.of(now.getYear(), 12, 31);
            case "ANNIVERSARY_YEAR":
                Month m = userCard.getOpenMonth() != null ? userCard.getOpenMonth() : Month.JANUARY;
                return calculateAnniversaryStart(m, now).plusYears(1).minusDays(1);
            default:
                return null;
        }
    }

    private LocalDate calculateAnniversaryStart(Month openMonth, LocalDate now) {
        LocalDate anniversary = LocalDate.of(now.getYear(), openMonth, 1);
        return anniversary.isAfter(now) ? anniversary.minusYears(1) : anniversary;
    }
}