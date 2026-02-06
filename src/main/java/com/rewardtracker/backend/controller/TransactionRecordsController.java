package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.service.TransactionRecordsService;
import com.rewardtracker.backend.model.TransactionRecords;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@CrossOrigin(origins = "https://reward-tracker-frontend.vercel.app")
@RestController
@RequestMapping("/transactionrecords")
public class TransactionRecordsController {
    private final TransactionRecordsService transactionRecordsService;

    public TransactionRecordsController(TransactionRecordsService transactionRecordsService) {
        this.transactionRecordsService = transactionRecordsService;
    }

    @PostMapping("/user/{userId}/card/{cardId}")
    public TransactionRecords addTransaction(
        @PathVariable Long userId,
        @PathVariable Long cardId,
        @RequestBody TransactionRecords record
    ) {
        return transactionRecordsService.saveWithDetails(userId, cardId, record);
    }

    @GetMapping("/user/{userId}/card/{cardId}")
    public List<TransactionRecords> getCardTransactions(@PathVariable Long userId, @PathVariable Long cardId) {
        return transactionRecordsService.getCardTransactions(userId, cardId);
    }

    @GetMapping("/user/{userId}")
    public List<TransactionRecords> getUserTransactions(@PathVariable Long userId) {
        return transactionRecordsService.getUserTransactions(userId);
    }

    @GetMapping("/user/{userId}/total-transaction")
    public Double getTotalTransactionFee(@PathVariable Long userId) {
        return transactionRecordsService.calculateTotalTransactionAmount(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteTransactionById(@PathVariable Long id) {
        transactionRecordsService.deleteTransactionById(id);
    }

}