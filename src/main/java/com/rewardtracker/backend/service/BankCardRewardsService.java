package com.rewardtracker.backend.service;

import com.rewardtracker.backend.model.*;
import com.rewardtracker.backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BankCardRewardsService {
    private final BankCardRewardsRepository bankCardRewardsRepository;

    public BankCardRewardsService(BankCardRewardsRepository bankCardRewardsRepository) {
        this.bankCardRewardsRepository = bankCardRewardsRepository;
    }

    public List<BankCardRewards> getAllCardRewards() {
        return bankCardRewardsRepository.findAll();
    }

    public List<BankCardRewards> getRewardsByBankCardId(Long bankCardId) {
        return bankCardRewardsRepository.findByBankCreditCard_Id(bankCardId);
    }

    public BankCardRewards saveReward(BankCardRewards reward) {
        return bankCardRewardsRepository.save(reward);
    }

    public void deleteReward(Long id) {
        bankCardRewardsRepository.deleteById(id);
    }
}