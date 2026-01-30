package com.rewardtracker.backend.repository;

import com.rewardtracker.backend.model.BankCardRewards;
import com.rewardtracker.backend.model.BankCreditCard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankCardRewardsRepository extends JpaRepository <BankCardRewards, Long> {

    List<BankCardRewards> findByBankCreditCard(BankCreditCard bankCreditCard);

}
