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

    private String merchantType;

    private String type;

    private Double rewardRate;

    private Double totalAmount;

    private String frequency;

    private Integer periods;

    private Double perPeriodAmount;

    private Integer maxUses;

    private Double minSpend;

    private String conditions;

    private Double decemberAmount;

    private String calculationType;

    private Long parentRewardId;

    public boolean isEligible(String transactionType) {
        if (transactionType == null || this.merchantType == null) return false;
        String tType = transactionType.toUpperCase();
        String rType = this.merchantType.toUpperCase();

        if (tType.equals(rType)) return true;
        if (rType.equals("ALL")) return true;

        if (tType.equals("CHASE_TRAVEL") && rType.equals("TRAVEL")) return true;
        
        if (tType.equals("CHASE_PORTAL_HOTEL")) {
            return rType.equals("CHASE_TRAVEL") || rType.equals("TRAVEL") || rType.equals("HOTEL");
        }

        return false;
    }

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

    public String getMerchantType() { 
        return merchantType; 
    }

    public Double getRewardRate() {
        return rewardRate;
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

    public Double getDecemberAmount() {
        return decemberAmount;
    }

    public String getCalculationType() {
        return calculationType;
    }

    public Long getParendRewardId() {
        return parentRewardId;
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

    public void setMerchantType(String merchantType) { 
        this.merchantType = merchantType; 
    }

    public void setRewardRate(Double rewardRate) {
        this.rewardRate = rewardRate;
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

    public void setDecemberAmount(Double decemberAmount) {
        this.decemberAmount = decemberAmount;
    }

    public void setCalculationType(String calculationType) {
        this.calculationType = calculationType;
    }

    public void setParentRewardId(Long parentRewardId) {
        this.parentRewardId = parentRewardId;
    }
}
