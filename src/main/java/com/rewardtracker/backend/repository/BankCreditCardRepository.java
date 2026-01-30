package com.rewardtracker.backend.repository;

import com.rewardtracker.backend.model.BankCreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BankCreditCardRepository extends JpaRepository<BankCreditCard, Long> {
    
    Optional<BankCreditCard> findByBankNameAndCardName(String bankName, String cardName);
    
    List<BankCreditCard> findByBankName(String bankName);

    List<BankCreditCard> findByAnnualFeeLessThanEqual(Integer fee);
}
