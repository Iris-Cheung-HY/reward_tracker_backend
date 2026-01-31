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
    TransactionRecords saved = transactionRecordsRepository.save(transactionRecords);
    }


    public List<TransactionRecords> getUserTransactions(Long userId) {
        return transactionRecordsRepository.findByUserId(userId);
    }

    public List<TransactionRecords> getCardTransactions(Long cardId) {
        return transactionRecordsRepository.findByUserCreditCardId(cardId);
    }
}
