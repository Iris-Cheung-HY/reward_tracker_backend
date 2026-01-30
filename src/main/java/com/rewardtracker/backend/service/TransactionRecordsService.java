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

    public TransactionRecords saveTransactionRecords(TransactionRecords tx) {
        return transactionRecordsRepository.save(tx);
    }

    public TransactionRecords saveTransactionRecords(TransactionRecords tx) {
    TransactionRecords saved = transactionRecordsRepository.save(tx);
    
    if (saved.getUserCreditCard() != null && saved.getUserCreditCard().getUser() != null) {
        Long userId = saved.getUserCreditCard().getUser().getId();
    }
    
    return saved;
    }


    public List<TransactionRecords> getUserTransactions(Long userId) {
        return transactionRecordsRepository.findByUserId(userId);
    }

    public List<TransactionRecords> getCardTransactions(Long cardId) {
        return transactionRecordsRepository.findByUserCreditCardId(cardId);
    }
}
