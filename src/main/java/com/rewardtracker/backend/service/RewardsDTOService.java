package com.rewardtracker.backend.service;

import com.rewardtracker.backend.model.*;
import com.rewardtracker.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
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

    public List<RewardsDTO> getCalculatedRewards(Long userCardId) {
        UserCreditCard userCard = userCreditCardRepository.findById(userCardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        
        Long bankCreditCardId = userCard.getBankCreditCard().getId();
        List<BankCardRewards> rules = bankCardRewardsRepository.findByBankCreditCard_Id(bankCreditCardId);
        List<TransactionRecords> transactions = transactionRecordsRepository.findByUserCreditCardId(userCardId);

        LocalDate now = LocalDate.now();
        Month openMonth = userCard.getOpenMonth() != null ? userCard.getOpenMonth() : now.getMonth();

        return rules.stream().map(rule -> {
            RewardsDTO dto = new RewardsDTO();
            dto.setMerchantType(rule.getMerchantType());
            dto.setRewardRate(rule.getRewardRate());
            dto.setType(rule.getType());
            dto.setConditions(rule.getConditions());

            boolean allCat = "ALL".equalsIgnoreCase(rule.getMerchantType());

            double effectiveSpent;
            
            if ("TIME".equalsIgnoreCase(rule.getCalculationType())) {
                LocalDate anniversaryDate = calculateAnniversaryStartFromMonth(openMonth, now);
                effectiveSpent = (!now.isBefore(anniversaryDate)) ? 1.0 : 0.0;
            } else {
                List<TransactionRecords> eligibleTxns = transactions.stream()
                    .filter(t -> t.getDate() != null)
                    .filter(t -> allCat || (t.getMerchantType() != null && t.getMerchantType().equalsIgnoreCase(rule.getMerchantType())))
                    .collect(Collectors.toList());

                if ("calendar_year".equalsIgnoreCase(rule.getFrequency())) {
                    effectiveSpent = eligibleTxns.stream()
                        .filter(t -> t.getDate().getYear() == now.getYear())
                        .mapToDouble(TransactionRecords::getAmount).sum();         
                } else if ("anniversary_year".equalsIgnoreCase(rule.getFrequency()) || 
                           "ANNIVERSARY".equalsIgnoreCase(rule.getCalculationType())) {
                    LocalDate anniversaryStart = calculateAnniversaryStartFromMonth(openMonth, now);
                    effectiveSpent = eligibleTxns.stream()
                        .filter(t -> !t.getDate().isBefore(anniversaryStart))
                        .mapToDouble(TransactionRecords::getAmount).sum();
                } else if ("MONTHLY".equalsIgnoreCase(rule.getFrequency())) {
                    effectiveSpent = eligibleTxns.stream()
                        .filter(t -> t.getDate().getMonth() == now.getMonth() && 
                                     t.getDate().getYear() == now.getYear())
                        .mapToDouble(TransactionRecords::getAmount).sum();
                } else if ("BI_ANNUALLY".equalsIgnoreCase(rule.getFrequency())) {
                    int startMonth = now.getMonthValue() <= 6 ? 1 : 7;
                    effectiveSpent = eligibleTxns.stream()
                        .filter(t -> t.getDate().getYear() == now.getYear())
                        .filter(t -> {
                            int m = t.getDate().getMonthValue();
                            return (startMonth == 1) ? (m >= 1 && m <= 6) : (m >= 7 && m <= 12);
                        })
                        .mapToDouble(TransactionRecords::getAmount).sum();
                } else {
                    effectiveSpent = eligibleTxns.stream().mapToDouble(TransactionRecords::getAmount).sum();
                }
            }

            double target = (rule.getTotalAmount() != null) ? rule.getTotalAmount() : 0.0;

            if (rule.getPerPeriodAmount() != null) {
                if ("MONTHLY".equalsIgnoreCase(rule.getFrequency())) {
                    target = (now.getMonth() == Month.DECEMBER && rule.getDecemberAmount() != null) 
                             ? rule.getDecemberAmount() : rule.getPerPeriodAmount();
                } else if ("BI_ANNUALLY".equalsIgnoreCase(rule.getFrequency())) {
                    target = rule.getPerPeriodAmount();
                }
            } else if ("BI_ANNUALLY".equalsIgnoreCase(rule.getFrequency()) && rule.getTotalAmount() != null) {
                target = rule.getTotalAmount() / 2;
            }

            dto.setUsedAmount(effectiveSpent);
            
            if (target > 0) {
                dto.setTotalAmount(target);
                dto.setRemainingAmount(Math.max(0, target - effectiveSpent));
                dto.setEligible(effectiveSpent >= target);
            } else {
                dto.setTotalAmount(null);
                dto.setRemainingAmount(0.0);
                dto.setEligible(true);
            }

            return dto;
        }).collect(Collectors.toList());
    }

    private LocalDate calculateAnniversaryStartFromMonth(Month openMonth, LocalDate now) {
        LocalDate thisYearAnniversary = LocalDate.of(now.getYear(), openMonth.getValue(), 1);
        if (thisYearAnniversary.isAfter(now)) {
            return thisYearAnniversary.minusYears(1);
        }
        return thisYearAnniversary;
    }
}