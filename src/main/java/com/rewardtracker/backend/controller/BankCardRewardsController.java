package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.service.*;
import com.rewardtracker.backend.model.*;
import com.rewardtracker.backend.repository.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@CrossOrigin(origins = "https://reward-tracker-frontend.vercel.app")
@RestController
@RequestMapping("/bankrewards")
public class BankCardRewardsController {
    private final BankCardRewardsService bankCardRewardsService;
    private final BankCardRewardsRepository bankCardRewardsRepository; // 直接注入 Repository 方便快速開發

    public BankCardRewardsController(BankCardRewardsService bankCardRewardsService, BankCardRewardsRepository bankCardRewardsRepository) {
        this.bankCardRewardsService = bankCardRewardsService;
        this.bankCardRewardsRepository = bankCardRewardsRepository;
    }

    @PostMapping
    public BankCardRewards addReward(@RequestBody BankCardRewards reward) {
        return bankCardRewardsRepository.save(reward);
    }

    @GetMapping
    public List<BankCardRewards> getAllRewards() {
        return bankCardRewardsService.getAllCardRewards();
    }

    @GetMapping("/categories")
    public List<String> getAllCategories() {
        return bankCardRewardsRepository.findAllDistinctMerchantTypes();
    }
}
