package com.rewardtracker.backend.service;

import com.rewardtracker.backend.model.*;
import com.rewardtracker.backend.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionRecordsService {
    private final TransactionRecordsRepository transactionRecordsRepository;
    private final UserLogRepository userLogRepository;
    private final UserCreditCardRepository userCreditCardRepository;

    public TransactionRecordsService(
        TransactionRecordsRepository transactionRecordsRepository,
        UserLogRepository userLogRepository,
        UserCreditCardRepository userCreditCardRepository) {
        this.transactionRecordsRepository = transactionRecordsRepository;
        this.userLogRepository = userLogRepository;
        this.userCreditCardRepository = userCreditCardRepository;
    }

    public TransactionRecords saveWithDetails(Long userId, Long cardId, TransactionRecords record) {
        UserLog user = userLogRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        UserCreditCard card = userCreditCardRepository.findById(cardId)
            .orElseThrow(() -> new RuntimeException("Card not found"));

        record.setUser(user);
        record.setUserCreditCard(card);
        
        return transactionRecordsRepository.save(record);
    }

    public List<TransactionRecords> getUserTransactions(Long userId) {
        return transactionRecordsRepository.findByUserId(userId);
    }

    public List<TransactionRecords> getCardTransactions(Long userId, Long cardId) {
        return transactionRecordsRepository.findByUserCreditCardId(cardId);
    }

    public Double calculateTotalTransactionAmount(Long userId) {
        Double total = transactionRecordsRepository.getTotalTransactionAmountByUserId(userId);
        return (total != null) ? total : 0.0;
    }
}