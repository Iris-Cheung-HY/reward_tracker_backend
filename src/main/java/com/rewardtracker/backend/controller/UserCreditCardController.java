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

    @PostMapping
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
}