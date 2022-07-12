/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Stjepan
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Product.getAll, query = "select p from Product p order by p.name"),
    @NamedQuery(name = Product.getAllBySalesObject, query = "select p from Product p where p.salesObject = :so order by p.name"),
    @NamedQuery(name = Product.getAllActive, query = "select p from Product p where p.disabled=false order by p.name")
})
public class Product implements IEntity, Serializable{
    
    public final static String getAll = "hr.workspace.models.Product.getAll";
    public final static String getAllBySalesObject = "hr.workspace.models.Product.getAllBySalesObject";
    public final static String getAllActive = "hr.workspace.models.Product.getAllActive";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "product_id_seq")
    private Long id;
    
    @ManyToOne
    private SalesObject salesObject;
    
    @Enumerated(EnumType.ORDINAL)
    private ProductType productType;
    
    private String name;
    
    private BigDecimal price;
    private Integer quantity;
    
    private Boolean disabled;

    public Product() {
        price = BigDecimal.ZERO;
        quantity = 0;
        disabled = false;
        
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SalesObject getSalesObject() {
        return salesObject;
    }

    public void setSalesObject(SalesObject salesObject) {
        this.salesObject = salesObject;
    }
    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.id.hashCode();
        hash = 29 * hash + (this.salesObject != null ? this.salesObject.hashCode() : 0);
        hash = 29 * hash + (this.productType != null ? this.productType.hashCode() : 0);
        hash = 29 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 29 * hash + (this.price != null ? this.price.hashCode() : 0);
        hash = 29 * hash + (this.quantity != null ? this.quantity.hashCode() : 0);
        hash = 29 * hash + (this.disabled != null ? this.disabled.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        Product other = (Product) object;
        if (this.getId() == null && other.getId() == null) {
            return super.equals(other);
        }
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    
        
}
