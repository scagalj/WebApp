/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Stjepan
 */
@Entity
public class OrderItem implements IEntity, Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "orderitem_id_seq")
    private Long id;
    
    @ManyToOne
    private UserOrder userOrder;
    
    @ManyToOne
    private Product product;
    
    private Integer quantity;
    private BigDecimal discount;
    private BigDecimal price;
    
    @OneToMany(mappedBy = "orderItem", fetch = FetchType.EAGER)
    private List<OrderItemDiscount> orderItemDiscounts;

    public OrderItem() {
        price = BigDecimal.ZERO;
        discount = BigDecimal.ZERO;
        quantity = 1;
        orderItemDiscounts = new ArrayList<>();
    }
    
    
     public BigDecimal getDiscountAmount() {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal finalPriceWithOutDiscount = getFinalPriceWithoutDiscount();
        List<OrderItemDiscount> tmpDiscounts = getOrderItemDiscounts();
        if (tmpDiscounts != null && !tmpDiscounts.isEmpty()) {
            for (OrderItemDiscount disc : tmpDiscounts) {
                result = result.add(disc.getAbsoluteAmountDiscount(finalPriceWithOutDiscount));
            }
        }
        return result;
    }

    public BigDecimal getDiscountAmountInPercentage() {
        BigDecimal percentage = getDiscountAmount().divide(getFinalPriceWithoutDiscount(), 6, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        if(percentage.compareTo(new BigDecimal(100)) > 0){
            percentage = new BigDecimal(100);
        }
        return percentage;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public BigDecimal getFinalPriceWithoutDiscount(){
        
        BigDecimal amount = getPrice().multiply(new BigDecimal(getQuantity()));
//        BigDecimal amount = price.subtract(getDiscountAmount());
        if(amount.compareTo(BigDecimal.ZERO) < 0){
            amount = BigDecimal.ZERO;
        }
        return amount;
    }
    public BigDecimal getFinalPrice(){
        
        BigDecimal price = getPrice().multiply(new BigDecimal(getQuantity()));
        BigDecimal amount = price.subtract(getDiscountAmount());
        if(amount.compareTo(BigDecimal.ZERO) < 0){
            amount = BigDecimal.ZERO;
        }
        return amount;
    }

    public List<OrderItemDiscount> getOrderItemDiscounts() {
        return orderItemDiscounts;
    }

    public void setOrderItemDiscounts(List<OrderItemDiscount> orderItemDiscounts) {
        this.orderItemDiscounts = orderItemDiscounts;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        OrderItem other = (OrderItem) object;
        if (this.getId() == null && other.getId() == null) {
            return super.equals(other);
        }
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
}
