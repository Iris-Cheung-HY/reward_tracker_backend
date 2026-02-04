package com.rewardtracker.backend.controller;

import com.rewardtracker.backend.service.BankCreditCardService;
import com.rewardtracker.backend.model.BankCreditCard;
import com.rewardtracker.backend.model.BankCreditCardDTO;

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

    @GetMapping
    public List<BankCreditCard> getAllBankCards() {
        return bankCreditCardService.getAllCardBankCreditCards();
    }

    @GetMapping("/banks")
    public List<String> getUniqueBanks() {
        return bankCreditCardService.getAllUniqueBanks();
    }

    @GetMapping("/bank/{bankName}/cards")
    public List<BankCreditCardDTO> getCardsByBank(@PathVariable String bankName) {
        return bankCreditCardService.getCardsByBankName(bankName);
    }

}