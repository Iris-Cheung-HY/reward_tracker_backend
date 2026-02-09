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

        Map<Long, Double> rawSpendMap = new HashMap<>();
        for (BankCardRewards rule : rules) {
            double raw = getRawSpendForRule(rule, allTransactions, rules, openMonth, now);
            rawSpendMap.put(rule.getId(), raw);
        }

        return rules.stream().map(rule -> {
            RewardsDTO dto = new RewardsDTO();
            dto.setMerchantType(rule.getMerchantType());
            dto.setRewardRate(rule.getRewardRate());
            dto.setType(rule.getType());
            dto.setConditions(rule.getConditions());

            double target = calculateTarget(rule, now);
            double used;


            if ("TIME".equalsIgnoreCase(rule.getCalculationType())) {
                LocalDate anniversaryDate = calculateAnniversaryStart(openMonth, now);
                used = (!now.isBefore(anniversaryDate)) ? 1.0 : 0.0;
            } 

            else if ("CREDIT".equalsIgnoreCase(rule.getType())) {
                double rawSpend = rawSpendMap.getOrDefault(rule.getId(), 0.0);
                used = Math.min(rawSpend, target); 
            }
            else if ("POINTS".equalsIgnoreCase(rule.getType())) {
                double rawSpend = rawSpendMap.getOrDefault(rule.getId(), 0.0);
                double totalDeduction = 0.0;

                if (rule.getParents() != null && !rule.getParents().isEmpty()) {
                    for (BankCardRewards parent : rule.getParents()) {
                        double parentRaw = rawSpendMap.getOrDefault(parent.getId(), 0.0);
                        double parentTarget = calculateTarget(parent, now);
                        totalDeduction += Math.min(parentRaw, parentTarget);
                    }
                }

                double finalBase = Math.max(0, rawSpend - totalDeduction);
                used = (rule.getRewardRate() != null) ? 
                       Math.round(finalBase * rule.getRewardRate() * 100.0) / 100.0 : 0.0;
            } 

            else {
                used = rawSpendMap.getOrDefault(rule.getId(), 0.0);
            }

            dto.setUsedAmount(used);

            if ("TIME".equalsIgnoreCase(rule.getCalculationType()) || target <= 0.0) {
                dto.setTotalAmount(null);
                dto.setRemainingAmount(0.0);
                dto.setEligible(used >= 1.0);
            } else {
                dto.setTotalAmount(target);
                dto.setRemainingAmount(Math.max(0, target - used));
                dto.setEligible(used >= target);
            }

            dto.setNextDueDate(calculateNextResetDate(rule, now));
            dto.setTotalPeriods(rule.getPeriods());

            return dto;
        }).collect(Collectors.toList());
    }

    private double getRawSpendForRule(BankCardRewards rule, List<TransactionRecords> allTransactions, List<BankCardRewards> rules, Month openMonth, LocalDate now) {
        String groupName = extractGroupName(rule.getConditions());
        List<TransactionRecords> eligibleTxns;

        if (groupName != null) {
            eligibleTxns = allTransactions.stream()
                .filter(t -> isTxnInGroup(t, rules, groupName))
                .collect(Collectors.toList());
        } 
        else if ("OTHERS".equalsIgnoreCase(rule.getMerchantType())) {
            eligibleTxns = allTransactions.stream()
                .filter(t -> !matchesAnySpecialRule(t, rules))
                .collect(Collectors.toList());
        } else { 
            eligibleTxns = allTransactions.stream()
                .filter(t -> rule.isEligible(t.getMerchantType()))
                .collect(Collectors.toList());
        }
        return calculateWindowedAmount(eligibleTxns, rule, openMonth, now);
    }

    private boolean matchesAnySpecialRule(TransactionRecords t, List<BankCardRewards> allRules) {
        return allRules.stream()
            .filter(r -> !"OTHERS".equalsIgnoreCase(r.getMerchantType()))
            .filter(r -> !"MILESTONE".equalsIgnoreCase(r.getType()))
            .anyMatch(r -> r.isEligible(t.getMerchantType()));
    }

    private String extractGroupName(String conditions) {
        if (conditions != null && conditions.contains("GROUP_")) {
            int start = conditions.indexOf("GROUP_");
            int end = conditions.indexOf(":", start);
            return end != -1 ? conditions.substring(start, end) : conditions.substring(start);
        }
        return null;
    }

    private boolean isTxnInGroup(TransactionRecords t, List<BankCardRewards> allRules, String groupName) {
        return allRules.stream()
            .filter(r -> groupName.equals(extractGroupName(r.getConditions())))
            .anyMatch(r -> r.isEligible(t.getMerchantType()));
    }

    private double calculateWindowedAmount(List<TransactionRecords> txns, BankCardRewards rule, Month openMonth, LocalDate now) {
        String freq = (rule.getFrequency() != null) ? rule.getFrequency().toUpperCase() : "";
        
        return txns.stream().filter(t -> {
            LocalDate d = t.getDate();
            if (d == null) return false;

            switch (freq) {
                case "MONTHLY":
                    return d.getMonth() == now.getMonth() && d.getYear() == now.getYear();
                case "QUARTERLY":
                    return (d.getMonthValue() - 1) / 3 == (now.getMonthValue() - 1) / 3 && d.getYear() == now.getYear();
                case "SEMI_ANNUAL":
                    boolean isFirstHalfNow = now.getMonthValue() <= 6;
                    boolean isFirstHalfTxn = d.getMonthValue() <= 6;
                    return (isFirstHalfNow == isFirstHalfTxn) && d.getYear() == now.getYear();
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
        String freq = rule.getFrequency() != null ? rule.getFrequency() : "";
        if ("MONTHLY".equalsIgnoreCase(freq) && now.getMonth() == Month.DECEMBER) {
            if (rule.getDecemberAmount() != null) return rule.getDecemberAmount();
        }
        if (rule.getPerPeriodAmount() != null) return rule.getPerPeriodAmount();
        return rule.getTotalAmount() != null ? rule.getTotalAmount() : 0.0;
    }

    private LocalDate calculateNextResetDate(BankCardRewards rule, LocalDate now) {
        String freq = rule.getFrequency() != null ? rule.getFrequency().toUpperCase() : "";
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
            default:
                return null;
        }
    }

    private LocalDate calculateAnniversaryStart(Month openMonth, LocalDate now) {
        LocalDate anniversary = LocalDate.of(now.getYear(), openMonth, 1);
        return anniversary.isAfter(now) ? anniversary.minusYears(1) : anniversary;
    }
}