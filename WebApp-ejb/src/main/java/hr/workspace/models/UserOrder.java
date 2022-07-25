/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author Stjepan
 */
@Entity
@NamedQueries({
    @NamedQuery(name = UserOrder.getAll, query = "select so from UserOrder so"),
    @NamedQuery(name = UserOrder.getAllActive, query = "select so from UserOrder so where so.disabled=false")
})
public class UserOrder implements IEntity, Serializable{
    
    public final static String getAll = "hr.workspace.models.UserOrder.getAll";
    public final static String getAllActive = "hr.workspace.models.UserOrder.getAllActive";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "userorder_id_seq")
    private Long id;
    
    @OneToMany(mappedBy = "userOrder", fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;
    
    @ManyToOne
    private SalesObject salesObject;
    
    @ManyToOne
    private ContactUser contactUser;
    
    @Enumerated(EnumType.ORDINAL)
    private UserOrderStatus userOrderStatus;

    @OneToMany(mappedBy = "userOrder", fetch = FetchType.EAGER)
    private List<Attachment> attachments;
    
    @OneToMany(mappedBy = "userOrder", fetch = FetchType.EAGER)
    private List<Payment> payments;
    
    private Boolean disabled;
    
    public UserOrder() {
        orderItems = new ArrayList<>();
        disabled = false;
        userOrderStatus = UserOrderStatus.INIT;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public ContactUser getUser() {
        return contactUser;
    }

    public void setUser(ContactUser user) {
        this.contactUser = user;
    }

    public SalesObject getSalesObject() {
        return salesObject;
    }

    public void setSalesObject(SalesObject salesObject) {
        this.salesObject = salesObject;
    }

    public ContactUser getContactUser() {
        return contactUser;
    }

    public void setContactUser(ContactUser contactUser) {
        this.contactUser = contactUser;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
    
    public BigDecimal getFinalPrice(){
        BigDecimal result = BigDecimal.ZERO;
        for(OrderItem orderItem : getOrderItems()){
            result = result.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        }
        return result;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public UserOrderStatus getUserOrderStatus() {
        return userOrderStatus;
    }

    public void setUserOrderStatus(UserOrderStatus userOrderStatus) {
        this.userOrderStatus = userOrderStatus;
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
        UserOrder other = (UserOrder) object;
        if (this.getId() == null && other.getId() == null) {
            return super.equals(other);
        }
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
}
