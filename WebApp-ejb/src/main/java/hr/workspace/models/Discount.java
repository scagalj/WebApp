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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Stjepan
 */
@Entity
public class Discount implements IEntity, Serializable {
    
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
    @ManyToMany()
    private List<ContactUser> contactUsers;
    @ManyToMany()
    private List<Product> products;

    public Discount() {
    
        contactUsers = new ArrayList<>();
        products = new ArrayList<>();
        promoCode = false;
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
