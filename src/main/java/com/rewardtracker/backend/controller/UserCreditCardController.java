package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.service.*;

import com.rewardtracker.backend.model.*;


import org.springframework.web.bind.annotation.*;

import java.time.Month;
import java.util.*;

@CrossOrigin(origins = "https://reward-tracker-frontend.vercel.app")
@RestController
@RequestMapping("/usercreditcard")
public class UserCreditCardController {
    private final UserCreditCardService userCreditCardService;

    public UserCreditCardController(UserCreditCardService userCreditCardService) {
        this.userCreditCardService = userCreditCardService;
    }

    @PostMapping("/user/{userId}")
    public UserCreditCard addUserCard(
        @PathVariable Long userId, 
        @RequestBody Map<String, Object> payload
    ) {
        UserCreditCard newCard = new UserCreditCard();
        newCard.setLastFourDigits((String) payload.get("lastFourDigits"));
        newCard.setOpenMonth(Month.valueOf((String) payload.get("openMonth")));
        
        Long bankCardId = Long.valueOf(payload.get("bankCardId").toString());
        
        return userCreditCardService.saveUserCardWithDetails(userId, bankCardId, newCard);
    }

    @GetMapping("/user/{userId}")
    public List<UserCreditCard> getUserCards(@PathVariable Long userId) {
        return userCreditCardService.getAllCreditCards(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteUserCard(@PathVariable Long id) {
        userCreditCardService.deleteCreditCardById(id);
    }

    @PostMapping("/user/{userId}/check-card")
    public Map<String, Boolean> checkCardAvailability(
        @PathVariable Long userId,
        @RequestBody Map<String, String> request
    ) {
        String lastFourDigits = request.get("lastFourDigits");
        boolean isDuplicate = userCreditCardService.findByLastFourDigits(userId, lastFourDigits).isPresent();
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate); 
        return response;
    }

    @GetMapping("/user/{userId}/total-annual-fee")
    public Double getTotalAnnualFee(@PathVariable Long userId) {
        return userCreditCardService.calculateTotalAnnualFee(userId);
    }


}