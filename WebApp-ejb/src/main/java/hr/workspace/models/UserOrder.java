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
import java.util.Optional;
import java.util.stream.Collectors;
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
public class UserOrder implements IEntity, Serializable, Comparable<UserOrder> {

    public final static String getAll = "hr.workspace.models.UserOrder.getAll";
    public final static String getAllActive = "hr.workspace.models.UserOrder.getAllActive";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "userorder_id_seq")
    private Long id;

    @OneToMany(mappedBy = "userOrder", fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "userOrder", fetch = FetchType.EAGER)
    private List<OrderDiscount> orderDiscounts;

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

    @OneToMany(mappedBy = "userOrder", fetch = FetchType.EAGER)
    private List<OrderRepresentative> orderRepresentatives;

    private Boolean disabled;

    public UserOrder() {
        orderItems = new ArrayList<>();
        orderDiscounts = new ArrayList<>();
        orderRepresentatives = new ArrayList<>();
        payments = new ArrayList<>();
        disabled = false;
        userOrderStatus = UserOrderStatus.INIT;
    }

    public String getAllProductsNameByComma() {
        StringBuilder result = new StringBuilder();
        getOrderItems().forEach(i -> result.append(i.getProduct().getName()).append(","));

        return result.toString();
    }

    public Boolean hasPromoCode() {
        return fetchPromoCode() != null;
    }

    public OrderDiscount fetchPromoCode() {
        Optional<OrderDiscount> discount = getOrderDiscounts().stream().filter(d -> d.getPromoCode()).findFirst();
        if (discount.isPresent()) {
            return discount.get();
        }
        return null;
    }

    public Boolean hasAddedRepresentative(Representative rep){
        List<OrderRepresentative> orderReps = getOrderRepresentatives().stream().filter(r -> r.getRepresentative().equals(rep)).collect(Collectors.toList());
        if(orderReps != null && !orderReps.isEmpty()){
            return true;
        }
        return false;
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

    public BigDecimal getFinalPriceWithOutDiscount() {
        BigDecimal result = BigDecimal.ZERO;
        for (OrderItem orderItem : getOrderItems()) {
            result = result.add(orderItem.getFinalPrice());
        }
        if (getOrderRepresentatives() != null && !getOrderRepresentatives().isEmpty()) {
            for (OrderRepresentative rep : getOrderRepresentatives()) {
                result = result.add(rep.getPrice());
            }
        }
        return result;
    }

    public BigDecimal getFinalPrice() {
        BigDecimal finalPrice = getFinalPriceWithOutDiscount();
        BigDecimal promoCodeDiscountAmount = getPromoCodeDiscountAmount();
        if (promoCodeDiscountAmount.compareTo(BigDecimal.ZERO) > 0) {
            finalPrice = finalPrice.subtract(promoCodeDiscountAmount).setScale(2, RoundingMode.HALF_UP);
        }

        return finalPrice;
    }

    public BigDecimal getPromoCodeDiscountAmount() {
        BigDecimal finalPriceWithOutDiscount = getFinalPriceWithOutDiscount();
        OrderDiscount promoCode = fetchPromoCode();
        if (promoCode != null) {
            return promoCode.getAbsoluteAmountDiscount(finalPriceWithOutDiscount);
        }
        return BigDecimal.ZERO;
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

    public List<OrderDiscount> getOrderDiscounts() {
        return orderDiscounts;
    }

    public void setOrderDiscounts(List<OrderDiscount> orderDiscounts) {
        this.orderDiscounts = orderDiscounts;
    }

    public List<OrderRepresentative> getOrderRepresentatives() {
        return orderRepresentatives;
    }

    public void setOrderRepresentatives(List<OrderRepresentative> orderRepresentatives) {
        this.orderRepresentatives = orderRepresentatives;
    }
    
    public boolean isOrderCompleted(){
        return UserOrderStatus.COMPLETED.equals(getUserOrderStatus());
    }
    public boolean isOrderAuthorized(){
        return UserOrderStatus.AUTHORIZED.equals(getUserOrderStatus());
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

    @Override
    public int compareTo(UserOrder o) {
        return o.getId().compareTo(this.getId());
    }

}
