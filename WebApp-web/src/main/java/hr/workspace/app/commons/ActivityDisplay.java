/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.app.commons;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Stjepan
 */
public class ActivityDisplay implements Comparable<ActivityDisplay>{
    
    private Date activityDate;
    private BigDecimal amount;
    private String activityType;

    public ActivityDisplay(Date activityDate, BigDecimal amount, String activityType) {
        this.activityDate = activityDate;
        this.amount = amount;
        this.activityType = activityType;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ActivityDisplay other = (ActivityDisplay) obj;
        if (!Objects.equals(this.activityType, other.activityType)) {
            return false;
        }
        if (!Objects.equals(this.activityDate, other.activityDate)) {
            return false;
        }
        if (!Objects.equals(this.amount, other.amount)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(ActivityDisplay o) {
        int compareTo = o.getActivityDate().compareTo(activityDate);
        if(compareTo != 0){
            return compareTo;
        }
        return 0;
    }
    
    
    
    
}
