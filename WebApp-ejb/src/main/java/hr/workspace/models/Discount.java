/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Stjepan
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Discount.getAll, query = "select d from Discount d"),
    @NamedQuery(name = Discount.getAllActive, query = "select d from Discount d where d.disabled=false"),
    @NamedQuery(name = Discount.getAllActiveDiscount, query = "select d from Discount d where d.disabled=false and d.promoCode = false")
})
public class Discount implements IEntity, Serializable {

    public final static String getAll = "hr.workspace.models.Discount.getAll";
    public final static String getAllActive = "hr.workspace.models.Discount.getAllActive";
    public final static String getAllActiveDiscount = "hr.workspace.models.Discount.getAllActiveDiscount";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "discount_id_seq")
    private Long id;

    private String name;
    private BigDecimal amount;
    @Enumerated(EnumType.ORDINAL)
    private DiscountType type;
    @Temporal(TemporalType.TIMESTAMP)
    private Date validFrom;
    @Temporal(TemporalType.TIMESTAMP)
    private Date validTo;
    private Boolean promoCode;
    private String promoCodeValue;
    private Long priority;
    @ManyToMany()
    private List<ContactUser> contactUsers;
    @ManyToMany()
    private List<Product> products;
    private Boolean disabled;

    public Discount() {

        contactUsers = new ArrayList<>();
        products = new ArrayList<>();
        promoCode = false;
        disabled = false;
    }

    public Boolean isValid(UserOrder order, ContactUser contactUser, Product product, Date validOnDate) {
        if(getDisabled()){
            return false;
        }
        
        if (validOnDate != null && !(validOnDate.after(getValidFrom()) && validOnDate.before(getValidTo()))) {
            return false;
        }

        if (contactUser != null && !getContactUsers().isEmpty() && !getContactUsers().contains(contactUser)) {
            return false;
        }
        if (!getPromoCode()) {
            if(product != null && !getProducts().isEmpty() && !getProducts().contains(product)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
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

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public List<ContactUser> getContactUsers() {
        return contactUsers;
    }

    public void setContactUsers(List<ContactUser> contactUsers) {
        this.contactUsers = contactUsers;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
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
        Discount other = (Discount) object;
        if (this.getId() == null && other.getId() == null) {
            return super.equals(other);
        }
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
