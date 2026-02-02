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

    @PostMapping
    public TransactionRecords addTransaction(@RequestBody TransactionRecords record) {
        return transactionRecordsService.saveTransactionRecords(record);
    }

    @GetMapping("/card/{userCardId}")
    public List<TransactionRecords> getCardTransactions(@PathVariable Long userCardId) {
        return transactionRecordsService.getCardTransactions(userCardId);
    }

    @GetMapping("/user/{userId}")
    public List<TransactionRecords> getUserTransactions(@PathVariable Long userId) {
        return transactionRecordsService.getUserTransactions(userId);
    }
}