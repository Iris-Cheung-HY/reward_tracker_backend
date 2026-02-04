package com.rewardtracker.backend.service;

import com.rewardtracker.backend.model.UserCreditCard;
import com.rewardtracker.backend.model.UserLog;
import com.rewardtracker.backend.repository.UserCreditCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<UserCreditCard> findByLastFourDigits(Long userId, String lastFourDigits) {
        return userCreditCardRepository.findByUserIdAndLastFourDigits(userId, lastFourDigits);
    }

    public Double calculateTotalAnnualFee(Long userId) {
    Double total = userCreditCardRepository.getTotalAnnualFeeByUserId(userId);
        return (total != null) ? total : 0.0;
    }

    

}
