package com.rewardtracker.backend.repository;

import com.rewardtracker.backend.model.UserCreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface UserCreditCardRepository extends JpaRepository<UserCreditCard, Long> {
    
    List<UserCreditCard> findByUserId(Long userId);

    Optional<UserCreditCard> findByUserIdAndLastFourDigits(Long userId, String lastFourDigits);

    @Query("SELECT SUM(bc.annualFee) FROM UserCreditCard ucc JOIN ucc.bankCreditCard bc WHERE ucc.user.id = :userId")
    Double getTotalAnnualFeeByUserId(@Param("userId") Long userId);
}


