package com.rewardtracker.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity

public class UserCreditCard {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserLog user;

    @ManyToOne
    @JoinColumn(name = "bank_credit_card_id")
    private BankCreditCard bankCreditCard;

    private LocalDate openDate;
    
    private Boolean isActive;

    // Getter
    public Long getId() { 
        return id;
     }

    public UserLog getUser() { 
        return user; 
    }
    public BankCreditCard getBankCreditCard() { 
        return bankCreditCard; 
    }

    public LocalDate getOpenDate() { 
        return openDate; 
    }

    public Boolean getIsActive() { 
        return isActive; 
    }

    // Setter
    public void setId(Long id) { 
        this.id = id; 
    }

    public void setUser(UserLog user) { 
        this.user = user; 
    }

    public void setBankCreditCard(BankCreditCard bankCreditCard) { 
        this.bankCreditCard = bankCreditCard; 
    }

    public void setOpenDate(LocalDate openDate) { 
        this.openDate = openDate; 
    }

    public void setIsActive(Boolean isActive) {
         this.isActive = isActive; 
    }
}


