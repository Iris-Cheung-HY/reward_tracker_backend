package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.service.TransactionRecordsService;
import com.rewardtracker.backend.model.TransactionRecords;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://reward-tracker-frontend.vercel.app/")
@RestController
@RequestMapping("/transactionrecords")



public class TransactionRecordsController {

    private final TransactionRecordsService transactionRecordsService;

    public TransactionRecordsController(TransactionRecordsService transactionRecordsService) {
        this.transactionRecordsService = transactionRecordsService;
    }


}
