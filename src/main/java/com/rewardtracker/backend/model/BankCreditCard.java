package com.rewardtracker.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity

public class BankCreditCard {

    @Id
    @GeneratedValue
    private Long id;

    private String bankName;

    private String cardName;

    private Double annualFee;

    private String cardImage;

    private String cardType;

    // Getter
    public Long getId() { 
        return id; 
    }

    public String getBankName() { 
        return bankName; 
    }

    public String getCardName() { 
        return cardName; 
    }
    
    public Double getAnnualFee() { 
        return annualFee; 
    }

    public String getCardImage() { 
        return cardImage; 
    }

    public String getCardType() {
        return cardType;
    }

    // Setter
    public void setId(Long id) { 
        this.id = id; 
    }

    public void setBankName(String bankName) { 
        this.bankName = bankName; 
    }

    public void setCardName(String cardName) { 
        this.cardName = cardName; 
    }

    public void setAnnualFee(Double annualFee) { 
        this.annualFee = annualFee; 
    }

    public void setCardImage(String cardImage) { 
        this.cardImage = cardImage; 
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

}
