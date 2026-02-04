package com.rewardtracker.backend.model;

public class BankCreditCardDTO {
    private Long id;
    private String bankName;
    private String cardName;
    private String cardType;

    public BankCreditCardDTO() {
    }

    public BankCreditCardDTO(Long id, String bankName, String cardName, String cardType) {
        this.id = id;
        this.bankName = bankName;
        this.cardName = cardName;
        this.cardType = cardType;
    }

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

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}