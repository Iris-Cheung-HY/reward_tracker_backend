package com.rewardtracker.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.*;
import java.time.*;

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

    @Enumerated(EnumType.STRING)
    private Month openMonth;

    @Column(length = 4)
    private String lastFourDigits;

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

    public Month getOpenMonth() { 
        return openMonth; 
    }

    public String getLastFourDigits() {
        return lastFourDigits;
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

    public void setopenMonth(Month openMonth) { 
        this.openMonth = openMonth; 
    }

    public void setLastFourDigits(String lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
    }
}


