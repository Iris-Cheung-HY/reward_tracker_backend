package com.rewardtracker.backend.repository;

import com.rewardtracker.backend.model.TransactionRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface TransactionRecordsRepository extends JpaRepository <TransactionRecords, Long> {
    List<TransactionRecords> findByUserId(Long userId);
    List<TransactionRecords> findByUserCreditCardId(Long userCreditCardId);

    @Query("SELECT COALESCE(SUM(tr.amount), 0.0) FROM TransactionRecords tr WHERE tr.user.id = :userId")
    Double getTotalTransactionAmountByUserId(@Param("userId") Long userId);

}
