package com.rewardtracker.backend.service;

import com.rewardtracker.backend.model.TransactionRecords;
import com.rewardtracker.backend.model.UserCreditCard;
import com.rewardtracker.backend.service.BankCardRewardsService;
import com.rewardtracker.backend.repository.TransactionRecordsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionRecordsService {
    private final TransactionRecordsRepository transactionRecordsRepository;

    public TransactionRecordsService(TransactionRecordsRepository transactionRecordsRepository) {
        this.transactionRecordsRepository = transactionRecordsRepository;
    }

    public TransactionRecords saveTransactionRecords(TransactionRecords transactionRecords) {
        return transactionRecordsRepository.save(transactionRecords);
    }


    public List<TransactionRecords> getUserTransactions(Long userId) {
        return transactionRecordsRepository.findByUserId(userId);
    }

    public List<TransactionRecords> getCardTransactions(Long cardId) {
        return transactionRecordsRepository.findByUserCreditCardId(cardId);
    }

    public Double calculateTotalTransactionAmount(Long userId) {
    Double total = transactionRecordsRepository.getTotalTransactionAmountByUserId(userId);
        return (total != null) ? total : 0.0;
    }
}
