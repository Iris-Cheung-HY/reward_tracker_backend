package com.rewardtracker.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity

public class BankCardRewards {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bank_credit_card_id")
    private BankCreditCard bankCreditCard;

    private String type;

    private String merchant;

    private Double totalAmount;

    private String frequency;

    private Integer periods;

    private Double perPeriodAmount;

    private Integer maxUses;

    private Double minSpend;

    private String conditions;

    // Getter
    public Long getId() { 
        return id;
    }

    public BankCreditCard getBankCreditCard() { 
        return bankCreditCard; 
    }

    public String getType() { 
        return type; 
    }

    public String getMerchant() { 
        return merchant; 
    }

    public Double getTotalAmount() { 
        return totalAmount; 
    }

    public String getFrequency() { 
        return frequency; 
    }

    public Integer getPeriods() { 
        return periods; 
    }

    public Double getPerPeriodAmount() { 
        return perPeriodAmount; 
    }

    public Integer getMaxUses() { 
        return maxUses; 
    }

    public Double getMinSpend() { 
        return minSpend; 
    }

    public String getConditions() { 
        return conditions; 
    }

    // Setter
    public void setId(Long id) { 
        this.id = id; 
    }

    public void setBankCreditCard(BankCreditCard bankCreditCard) { 
        this.bankCreditCard = bankCreditCard; 
    }

    public void setType(String type) { 
        this.type = type; 
    }

    public void setMerchant(String merchant) { 
        this.merchant = merchant; 
    }

    public void setTotalAmount(Double totalAmount) { 
        this.totalAmount = totalAmount; 
    }

    public void setFrequency(String frequency) { 
        this.frequency = frequency; 
    }

    public void setPeriods(Integer periods) { 
        this.periods = periods; 
    }

    public void setPerPeriodAmount(Double perPeriodAmount) { 
        this.perPeriodAmount = perPeriodAmount; 
    }

    public void setMaxUses(Integer maxUses) { 
        this.maxUses = maxUses; 
    }

    public void setMinSpend(Double minSpend) { 
        this.minSpend = minSpend; 
    }

    public void setConditions(String conditions) { 
        this.conditions = conditions; 
    }
}
