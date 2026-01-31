package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.service.UserCreditCardService;
import com.rewardtracker.backend.model.UserCreditCard;

import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/usercreditcard")

public class UserCreditCardController {

    private final UserCreditCardService userCreditCardService;

    public UserCreditCardController(UserCreditCardService userCreditCardService) {
        this.userCreditCardService = userCreditCardService;
    }

}
