package com.rewardtracker.backend.service;

import com.rewardtracker.backend.model.*;
import com.rewardtracker.backend.repository.*;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class UserCreditCardService {

    private final UserCreditCardRepository userCreditCardRepository;
    private final BankCreditCardRepository bankCreditCardRepository;
    private final UserLogRepository userLogRepository;

    public UserCreditCardService(
        UserCreditCardRepository userCreditCardRepository, 
        BankCreditCardRepository bankCreditCardRepository,
        UserLogRepository userLogRepository
    ) {
        this.userCreditCardRepository = userCreditCardRepository;
        this.bankCreditCardRepository = bankCreditCardRepository;
        this.userLogRepository = userLogRepository;
    }

    public UserCreditCard saveUserCardWithDetails(Long userId, Long bankCardId, UserCreditCard cardData) {
    UserLog user = userLogRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Couldn't find the user"));
    
    BankCreditCard bankCard = bankCreditCardRepository.findById(bankCardId)
        .orElseThrow(() -> new RuntimeException("Couldn't find the card"));
    
    cardData.setUser(user);
    cardData.setBankCreditCard(bankCard);
    
    return userCreditCardRepository.save(cardData);
    }

    public List<UserCreditCard> getAllCreditCards(Long userId) {
        return userCreditCardRepository.findByUserId(userId);
    }

    public void deleteCreditCardById (Long id) {
        userCreditCardRepository.deleteById(id);
    }

    public Optional<UserCreditCard> findByLastFourDigits(Long userId, String lastFourDigits) {
        return userCreditCardRepository.findByUserIdAndLastFourDigits(userId, lastFourDigits);
    }

    public Double calculateTotalAnnualFee(Long userId) {
    Double total = userCreditCardRepository.getTotalAnnualFeeByUserId(userId);
        return (total != null) ? total : 0.0;
    }

    

}
