package com.rewardtracker.backend.model;

import java.time.LocalDate;

public class RewardsDTO {

    private String merchantType;

    private String type;

    private Double rewardRate;

    private Double totalAmount;

    private Double usedAmount;

    private Double remainingAmount;

    private Double lostAmount;

    private Integer totalPeriods;

    private Integer remainingPeriods;

    private LocalDate nextDueDate;

    private Boolean eligible; 

    // Getter
    public String getMerchantType() { 
        return merchantType; 
    }

    public String getType() {
        return type;
    }

    public Double getrewardRate() {
        return rewardRate;
    }

    public Double getTotalAmount() { 
        return totalAmount; 
    }

    public Double getUsedAmount() { 
        return usedAmount; 
    }

    public Double getRemainingAmount() { 
        return remainingAmount; 
    }

    public Double getLostAmount() { 
        return lostAmount; 
    }

    public Integer getTotalPeriods() { 
        return totalPeriods; 
    }

    public Integer getRemainingPeriods() { 
        return remainingPeriods; 
    }

    public LocalDate getNextDueDate() { 
        return nextDueDate; 
    }

    public Boolean getEligible() { 
        return eligible; 
    }

    // Setter
    public void setMerchantType (String merchantType) { 
        this.merchantType = merchantType; 
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRewardRate (Double rewardRate) {
        this.rewardRate = rewardRate;
    }

    public void setTotalAmount(Double totalAmount) { 
        this.totalAmount = totalAmount; 
    }

    public void setUsedAmount(Double usedAmount) { 
        this.usedAmount = usedAmount; 
    }

    public void setRemainingAmount(Double remainingAmount) { 
        this.remainingAmount = remainingAmount; 
    }

    public void setLostAmount(Double lostAmount) { 
        this.lostAmount = lostAmount; 
    }

    public void setTotalPeriods(Integer totalPeriods) { 
        this.totalPeriods = totalPeriods; 
    }

    public void setRemainingPeriods(Integer remainingPeriods) { 
        this.remainingPeriods = remainingPeriods; 
    }

    public void setNextDueDate(LocalDate nextDueDate) { 
        this.nextDueDate = nextDueDate; 
    }

    public void setEligible(Boolean eligible) { 
        this.eligible = eligible; 
    }

}
