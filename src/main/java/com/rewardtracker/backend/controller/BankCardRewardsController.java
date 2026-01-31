package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.service.BankCardRewardsService;
import com.rewardtracker.backend.model.BankCardRewards;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/bankrewards")


public class BankCardRewardsController {

    private final BankCardRewardsService bankCardRewardsService;

    public BankCardRewardsController(BankCardRewardsService bankCardRewardsService) {
        this.bankCardRewardsService = bankCardRewardsService;
    }




}
