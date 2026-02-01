package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.service.BankCreditCardService;
import com.rewardtracker.backend.model.BankCreditCard;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@CrossOrigin(origins = "https://reward-tracker-frontend.vercel.app")
@RestController
@RequestMapping("/bankcreditcard")
public class BankCreditCardController {
    private final BankCreditCardService bankCreditCardService;

    public BankCreditCardController(BankCreditCardService bankCreditCardService) {
        this.bankCreditCardService = bankCreditCardService;
    }

    @PostMapping
    public BankCreditCard addBankCard(@RequestBody BankCreditCard bankCreditCard) {
        return bankCreditCardService.saveBankCreditCard(bankCreditCard);
    }

    @GetMapping("/all")
    public List<BankCreditCard> getAllBankCards() {
        return bankCreditCardService.getAllCardBankCreditCards();
    }
}