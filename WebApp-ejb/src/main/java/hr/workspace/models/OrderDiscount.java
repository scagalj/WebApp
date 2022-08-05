/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.models;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Stjepan
 */
@Entity
public class OrderDiscount implements IEntity, Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "orderdiscount_id_seq")
    private Long id;
    
    @ManyToOne
    private UserOrder userOrder;
    
    @ManyToOne
    private Discount discount;
    
    private String name;
    private BigDecimal amount;
    @Enumerated(EnumType.ORDINAL)
    private DiscountType type;
    private Boolean promoCode;
    private String promoCodeValue;

    public BigDecimal getAbsoluteAmountDiscount(BigDecimal price){
        if(DiscountType.AMOUNT.equals(getType())){
            return getAmount();
        }
        return getAmount().divide(new BigDecimal(100)).multiply(price);
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserOrder getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(UserOrder userOrder) {
        this.userOrder = userOrder;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public DiscountType getType() {
        return type;
    }

    public void setType(DiscountType type) {
        this.type = type;
    }

    public Boolean getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(Boolean promoCode) {
        this.promoCode = promoCode;
    }

    public String getPromoCodeValue() {
        return promoCodeValue;
    }

    public void setPromoCodeValue(String promoCodeValue) {
        this.promoCodeValue = promoCodeValue;
    }
    
    
    
}
