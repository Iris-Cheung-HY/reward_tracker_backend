package com.rewardtracker.backend.service;

import com.rewardtracker.backend.model.*;
import com.rewardtracker.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankCardRewardsService {
    private final BankCardRewardsRepository bankCardRewardsRepository;
    private final TransactionRecordsRepository transactionRecordsRepository;

    public BankCardRewardsService(BankCardRewardsRepository bankCardRewardsRepository, TransactionRecordsRepository transactionRecordsRepository) {
        this.bankCardRewardsRepository = bankCardRewardsRepository;
        this.transactionRecordsRepository = transactionRecordsRepository;
    }

    public List<RewardsDTO> getUserRewards (Long userId) {
        return List.of();
    }

    private List<RewardsDTO> calculateCardRewards(UserCreditCard card) {
        Month openMonth = card.getOpenMonth();
        LocalDate today = LocalDate.now();
        
        List<BankCardRewards> rewards = bankCardRewardsRepository
            .findByBankCreditCard(card.getBankCreditCard());
        
        return rewards.stream()
            .map(reward -> calculateSingleReward(reward, openMonth, today))
            .collect(Collectors.toList());
    }

    

    private RewardsDTO calculateSingleReward(BankCardRewards reward, Month openMonth, LocalDate today) {
        RewardsDTO dto = new RewardsDTO();
        dto.setMerchant(reward.getMerchant());
        dto.setTotalAmount(reward.getTotalAmount());
        
        if ("calendar_year".equals(reward.getFrequency())) {
            int startMonth = (openMonth != null) ? openMonth.getValue() : 1;
            
            dto.setRemainingPeriods(13 - startMonth);
            dto.setLostAmount(reward.getPerPeriodAmount() * (startMonth - 1));
        }
        
        dto.setNextDueDate(today.plusMonths(1));
        return dto;
    }

    public List<BankCardRewards> getAllCardRewards() {
        return bankCardRewardsRepository.findAll();
    }
}