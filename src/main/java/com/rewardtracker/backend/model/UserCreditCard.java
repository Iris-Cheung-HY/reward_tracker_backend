package com.rewardtracker.backend.model;

import jakarta.persistence.*;
import java.time.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @Column(name = "is_activate", nullable = false)
    private Boolean isActivate = true;

    @OneToMany(mappedBy = "userCreditCard", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore 
    private List<TransactionRecords> transactions;

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

    public Boolean getIsActivate() {
        return isActivate;
    }

    public List<TransactionRecords> getTransactions() {
        return transactions;
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

    public void setOpenMonth(Month openMonth) { 
        this.openMonth = openMonth; 
    }

    public void setLastFourDigits(String lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
    }

    public void setIsActivate(Boolean isActivate) {
        this.isActivate = isActivate;
    }

    public void setTransactions(List<TransactionRecords> transactions) {
        this.transactions = transactions;
    }
}


