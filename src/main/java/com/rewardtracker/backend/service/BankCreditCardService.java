package com.rewardtracker.backend.service;

import com.rewardtracker.backend.model.BankCreditCard;
import com.rewardtracker.backend.repository.BankCreditCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankCreditCardService {

    private final BankCreditCardRepository bankCreditCardRepository;

    public BankCreditCardService (BankCreditCardRepository bankCreditCardRepository) {
        this.bankCreditCardRepository = bankCreditCardRepository;
    }

    public BankCreditCard saveBankCreditCard(BankCreditCard bankCreditCard) {
        return bankCreditCardRepository.save(bankCreditCard);
    }

    public List<BankCreditCard> getAllCardBankCreditCards() {
        return bankCreditCardRepository.findAll();
    }

    public void deleteBankCardRewardsById(Long id) {
        bankCreditCardRepository.deleteById(id);
    }

}
