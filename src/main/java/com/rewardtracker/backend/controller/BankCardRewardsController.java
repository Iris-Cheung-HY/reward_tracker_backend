package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.service.*;
import com.rewardtracker.backend.model.*;
import com.rewardtracker.backend.repository.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@CrossOrigin(origins = "https://reward-tracker-frontend.vercel.app")
@RestController
@RequestMapping("/bankrewards")
public class BankCardRewardsController {
    private final BankCardRewardsService bankCardRewardsService;
    private final BankCardRewardsRepository bankCardRewardsRepository;
    private final RewardsDTOService rewardsDTOService;

    public BankCardRewardsController(
        BankCardRewardsService bankCardRewardsService, 
        BankCardRewardsRepository bankCardRewardsRepository,
        RewardsDTOService rewardsDTOService) {
        this.bankCardRewardsService = bankCardRewardsService;
        this.bankCardRewardsRepository = bankCardRewardsRepository;
        this.rewardsDTOService = rewardsDTOService;
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

    @GetMapping("/card/{id}/benefits")
    public ResponseEntity<List<RewardsDTO>> getBenefits(@PathVariable Long id) {
        return ResponseEntity.ok(rewardsDTOService.getCalculatedRewards(id));
    }

    @GetMapping("/categories/spend")
    public List<String> getSpendCategories() {
        return bankCardRewardsRepository.findAllSpendMerchantTypes();
    }
    
    @GetMapping("/categories/credits")
    public List<String> getCreditCategories() {
        return bankCardRewardsRepository.findAllDistinctMerchantTypes().stream()
            .filter(type -> type.contains("CREDIT") || type.contains("AMEX") || type.contains("GROUP"))
            .collect(Collectors.toList());
    }
}
