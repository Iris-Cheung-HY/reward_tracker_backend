package com.rewardtracker.backend.model;

import java.time.LocalDate;

public class RewardsDTO {

    private String merchant;

    private Double totalAmount;

    private Double usedAmount;

    private Double remainingAmount;

    private Double lostAmount;

    private Integer totalPeriods;

    private Integer remainingPeriods;

    private LocalDate nextDueDate;

    private Boolean eligible; 

    // Getter
    public String getMerchant() { 
        return merchant; 
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
    public void setMerchant(String merchant) { 
        this.merchant = merchant; 
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
