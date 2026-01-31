package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.service.BankCreditCardService;
import com.rewardtracker.backend.model.BankCreditCard;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/bankcreditcard")


public class BankCreditCardController {

    private final BankCreditCardService bankCreditCardService;

    public BankCreditCardController (BankCreditCardService bankCreditCardService) {
        this.bankCreditCardService = bankCreditCardService;
    }


}
