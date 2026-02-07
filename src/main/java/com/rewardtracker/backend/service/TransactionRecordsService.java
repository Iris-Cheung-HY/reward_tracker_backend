package com.rewardtracker.backend.service;

import com.rewardtracker.backend.model.*;
import com.rewardtracker.backend.repository.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

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
        UserLog user = userLogRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        UserCreditCard card = userCreditCardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
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

    public void deleteTransactionById(Long id) {
        transactionRecordsRepository.deleteById(id);
    }

    public Map<String, Double> calculateSpendingDistributions(Long cardId, List<String> specificMerchantTypes) {
        List<TransactionRecords> transactions = transactionRecordsRepository.findByUserCreditCardId(cardId);
        Map<String, Double> distributions = new HashMap<>();
        double othersTotal = 0.0;
        double allTotal = 0.0;

        Set<String> specificTypes = specificMerchantTypes.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toSet());

        for (TransactionRecords t : transactions) {
            double amount = (t.getAmount() != null) ? t.getAmount() : 0.0;
            String mType = (t.getMerchantType() != null) ? t.getMerchantType().toUpperCase() : "UNKNOWN";
            allTotal += amount;

            if (specificTypes.contains(mType)) {
                distributions.put(mType, distributions.getOrDefault(mType, 0.0) + amount);
            } else if (!mType.equals("ALL")) {
                othersTotal += amount;
            }
        }
        distributions.put("ALL", allTotal);
        distributions.put("OTHERS", othersTotal);
        return distributions;
    }

    public Double calculateTotalTransactionAmount(Long userId) {
        Double total = transactionRecordsRepository.sumAmountByUserId(userId);
        return total != null ? total : 0.0;
    }
}