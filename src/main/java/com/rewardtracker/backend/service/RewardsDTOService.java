package com.rewardtracker.backend.service;

import com.rewardtracker.backend.model.*;
import com.rewardtracker.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        double totalSpent = transactions.stream().mapToDouble(TransactionRecords::getAmount).sum();

    return rules.stream().map(rule -> {
        RewardsDTO dto = new RewardsDTO();
        dto.setMerchantType(rule.getMerchantType());
        dto.setRewardRate(rule.getRewardRate());
        dto.setTotalAmount(rule.getTotalAmount());
        dto.setType(rule.getType());

        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();

        if (rule.getRewardRate() != null && rule.getRewardRate() > 0) {
            double categorySpent = transactions.stream()
                .filter(t -> t.getMerchantType() != null)
                .filter(t -> t.getAmount() != null)
                .filter(t -> t.getMerchantType().equalsIgnoreCase(rule.getMerchantType()))
                .mapToDouble(TransactionRecords::getAmount).sum();
            
            dto.setUsedAmount(categorySpent * rule.getRewardRate()); 
            dto.setEligible(true);
        }

        if (rule.getTotalAmount() != null && rule.getTotalAmount() > 0) {
            dto.setUsedAmount(totalSpent); 
            dto.setRemainingAmount(Math.max(0, rule.getTotalAmount() - totalSpent));
            dto.setEligible(totalSpent >= rule.getTotalAmount());
        }

        if ("MONTHLY".equalsIgnoreCase(rule.getFrequency())) {
        
        double monthlyTarget = (currentMonth == 12 && rule.getDecemberAmount() != null) 
                                ? rule.getDecemberAmount() 
                                : rule.getPerPeriodAmount();
    
        dto.setTotalAmount(monthlyTarget);
        double spentThisMonth = transactions.stream()
            .filter(t -> t.getAmount() != null)
            .filter(t -> t.getDate() != null && 
                    t.getDate().getMonthValue() == currentMonth &&
                    t.getDate().getYear() == now.getYear())
            .filter(t -> t.getMerchantType().equalsIgnoreCase(rule.getMerchantType()))
            .mapToDouble(TransactionRecords::getAmount)
            .sum();

        dto.setUsedAmount(Math.min(spentThisMonth, monthlyTarget));
        dto.setRemainingAmount(Math.max(0, monthlyTarget - spentThisMonth));
        
        dto.setLostAmount(spentThisMonth < monthlyTarget ? monthlyTarget - spentThisMonth : 0.0);
        
        dto.setEligible(spentThisMonth >= monthlyTarget);
        dto.setNextDueDate(now.withDayOfMonth(now.lengthOfMonth()));
        }

        return dto;
        }).collect(Collectors.toList());
    }
}