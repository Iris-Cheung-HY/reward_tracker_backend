package com.rewardtracker.backend.service;

import com.rewardtracker.backend.model.BankCreditCard;
import com.rewardtracker.backend.model.BankCreditCardDTO;
import com.rewardtracker.backend.repository.BankCreditCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> getAllUniqueBanks() {
        return bankCreditCardRepository.findAll().stream()
                .map(BankCreditCard::getBankName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<BankCreditCardDTO> getCardsByBankName(String bankName) {
        return bankCreditCardRepository.findByBankName(bankName).stream()
                .map(card -> new BankCreditCardDTO(
                    card.getId(), 
                    card.getBankName(), 
                    card.getCardName(),
                    card.getCardType()
                ))
                .collect(Collectors.toList());
    }

}
