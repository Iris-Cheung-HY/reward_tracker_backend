package com.rewardtracker.backend.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TransactionRecords {

    @Id
    @GeneratedValue

    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_credit_card_id")
    private UserCreditCard userCreditCard;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserLog user;

    private String category;
    
    private Double amount; 

    private String description;

    private LocalDate date;

        // Getter
    public Long getId() { 
        return id; 
    }

    public UserCreditCard getUserCreditCard() { 
        return userCreditCard; 
    }

    public UserLog getUser() {
        return user;
    }

    public String getCategory() { 
        return category; 
    }

    public Double getAmount() { 
        return amount; 
    }

    public LocalDate getDate() { 
        return date; 
    }

    public String getDescription() { 
        return description; 
    }

    // Setter
    public void setId(Long id) { 
        this.id = id; 
    }

    public void setUserCreditCard(UserCreditCard userCreditCard) { 
        this.userCreditCard = userCreditCard; 
    }

    public void setUser(UserLog user) { 
        this.user = user; 
    }

    public void setCategory(String category) { 
        this.category = category; 
    }

    public void setAmount(Double amount) { 
        this.amount = amount; 
    }

    public void setDescription(String description) { 
        this.description = description; 
    }

    public void setDate(LocalDate date) { 
        this.date = date; 
    }
}
