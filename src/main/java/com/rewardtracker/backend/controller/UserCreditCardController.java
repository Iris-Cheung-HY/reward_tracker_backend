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

        Object openMonthObj = payload.get("openMonth");
        if (openMonthObj == null) {
            throw new IllegalArgumentException("Missing required field: openMonth");
        }

        UserCreditCard newCard = new UserCreditCard();
        newCard.setLastFourDigits(String.valueOf(payload.get("lastFourDigits")));
        

        String monthStr = openMonthObj.toString().toUpperCase().trim();
        newCard.setOpenMonth(Month.valueOf(monthStr));
        

        Object bankCardIdObj = payload.get("bankCardId");
        if (bankCardIdObj == null) {
            throw new IllegalArgumentException("Missing required field: bankCardId");
        }
        Long bankCardId = Long.valueOf(bankCardIdObj.toString());
        
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