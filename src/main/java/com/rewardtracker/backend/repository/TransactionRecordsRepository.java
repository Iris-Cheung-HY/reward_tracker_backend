package com.rewardtracker.backend.repository;

import com.rewardtracker.backend.model.TransactionRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface TransactionRecordsRepository extends JpaRepository <TransactionRecords, Long> {
    List<TransactionRecords> findByUserId(Long userId);
    List<TransactionRecords> findByUserCreditCardId(Long userCreditCardId);


}
