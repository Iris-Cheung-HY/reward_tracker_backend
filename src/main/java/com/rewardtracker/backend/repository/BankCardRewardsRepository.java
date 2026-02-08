package com.rewardtracker.backend.repository;

import com.rewardtracker.backend.model.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankCardRewardsRepository extends JpaRepository<BankCardRewards, Long> {

    List<BankCardRewards> findByBankCreditCard_Id(Long bankCreditCardId);

    @Query("SELECT DISTINCT r.merchantType FROM BankCardRewards r")
    List<String> findAllDistinctMerchantTypes();

    @Query("SELECT DISTINCT r.merchantType FROM BankCardRewards r WHERE r.bankCreditCard.id = :bankCardId")
    List<String> findDistinctMerchantTypesByBankCardId(@Param("bankCardId") Long bankCardId);

    @Query("SELECT DISTINCT r.merchantType FROM BankCardRewards r WHERE r.calculationType = 'SPEND'")
    List<String> findAllSpendMerchantTypes();
}
