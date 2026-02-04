package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.service.UserCreditCardService;
import com.rewardtracker.backend.model.UserCreditCard;
import org.springframework.web.bind.annotation.*;

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
    public UserCreditCard addUserCard(@RequestBody UserCreditCard userCreditCard) {
        return userCreditCardService.saveCreditCard(userCreditCard);
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
        response.put("isDuplicate", !isDuplicate); 
        return response;
    }

    @GetMapping("/user/{userId}/total-annual-fee")
    public Double getTotalAnnualFee(@PathVariable Long userId) {
        return userCreditCardService.calculateTotalAnnualFee(userId);
    }


}