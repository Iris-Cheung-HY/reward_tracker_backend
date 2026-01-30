package com.rewardtracker.backend.service;

import com.rewardtracker.backend.model.UserCreditCard;
import com.rewardtracker.backend.repository.UserCreditCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class UserCreditCardService {

    private final UserCreditCardRepository userCreditCardRepository;

    public UserCreditCardService(UserCreditCardRepository userCreditCardRepository) {
        this.userCreditCardRepository = userCreditCardRepository;
    }

    public UserCreditCard saveCreditCard(UserCreditCard userCreditCard) {
        return userCreditCardRepository.save(userCreditCard);
    }

    public List<UserCreditCard> getAllCreditCards(Long userId) {
        return userCreditCardRepository.findByUserId(userId);
    }

    public void deleteCreditCardById (Long id) {
        userCreditCardRepository.deleteById(id);
    }

    

}
