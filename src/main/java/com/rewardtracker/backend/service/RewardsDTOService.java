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
        UserCreditCard userCard = userCreditCardRepository.findById(userCardId).orElseThrow(() -> new RuntimeException("Card not found"));
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

            double effectiveValue;
            
            if ("TIME".equalsIgnoreCase(rule.getCalculationType())) {
                LocalDate anniversaryDate = calculateAnniversaryStartFromMonth(openMonth, now);
                effectiveValue = (!now.isBefore(anniversaryDate)) ? 1.0 : 0.0;
            } else {
                List<TransactionRecords> eligibleTxns = transactions.stream()
                    .filter(t -> t.getDate() != null && rule.isEligible(t.getMerchantType()))
                    .collect(Collectors.toList());

                double baseAmount = calculateWindowedAmount(eligibleTxns, rule, openMonth, now);
                
                effectiveValue = (rule.getRewardRate() != null && rule.getRewardRate() > 0) 
                                 ? baseAmount * rule.getRewardRate() 
                                 : baseAmount;
            }

            double target = calculateTarget(rule, now);
            dto.setUsedAmount(effectiveValue);
            
            if (target > 0) {
                dto.setTotalAmount(target);
                dto.setRemainingAmount(Math.max(0, target - effectiveValue));
                dto.setEligible(effectiveValue >= target);
            } else {
                dto.setTotalAmount(null);
                dto.setRemainingAmount(0.0);
                dto.setEligible(true);
            }
            return dto;
        }).collect(Collectors.toList());
    }

    private double calculateWindowedAmount(List<TransactionRecords> txns, BankCardRewards rule, Month openMonth, LocalDate now) {
        if ("calendar_year".equalsIgnoreCase(rule.getFrequency())) {
            return txns.stream().filter(t -> t.getDate().getYear() == now.getYear()).mapToDouble(TransactionRecords::getAmount).sum();
        } else if ("anniversary_year".equalsIgnoreCase(rule.getFrequency())) {
            LocalDate anniversaryStart = calculateAnniversaryStartFromMonth(openMonth, now);
            return txns.stream().filter(t -> !t.getDate().isBefore(anniversaryStart)).mapToDouble(TransactionRecords::getAmount).sum();
        } else if ("MONTHLY".equalsIgnoreCase(rule.getFrequency())) {
            return txns.stream().filter(t -> t.getDate().getMonth() == now.getMonth() && t.getDate().getYear() == now.getYear()).mapToDouble(TransactionRecords::getAmount).sum();
        }
        return txns.stream().mapToDouble(TransactionRecords::getAmount).sum();
    }

    private double calculateTarget(BankCardRewards rule, LocalDate now) {
        double target = (rule.getTotalAmount() != null) ? rule.getTotalAmount() : 0.0;
        if ("MONTHLY".equalsIgnoreCase(rule.getFrequency()) && rule.getPerPeriodAmount() != null) {
            target = (now.getMonth() == Month.DECEMBER && rule.getDecemberAmount() != null) ? rule.getDecemberAmount() : rule.getPerPeriodAmount();
        }
        return target;
    }

    private LocalDate calculateAnniversaryStartFromMonth(Month openMonth, LocalDate now) {
        LocalDate thisYearAnniversary = LocalDate.of(now.getYear(), openMonth.getValue(), 1);
        return thisYearAnniversary.isAfter(now) ? thisYearAnniversary.minusYears(1) : thisYearAnniversary;
    }
}