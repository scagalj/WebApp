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
import javax.persistence.Transient;

/**
 *
 * @author Stjepan
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Product.getAll, query = "select p from Product p order by p.name"),
    @NamedQuery(name = Product.getAllActive, query = "select p from Product p where p.disabled=false order by p.name")
})
public class Product implements IEntity, Serializable {

    public final static String getAll = "hr.workspace.models.Product.getAll";
    public final static String getAllActive = "hr.workspace.models.Product.getAllActive";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "product_id_seq")
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private ProductType productType;

    private String name;

    private BigDecimal price;
    private Integer quantity;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Attachment> attachments;

    private Boolean disabled;

    @Transient
    private List<OrderItemDiscount> tmpDiscounts;

    public Product() {
        price = BigDecimal.ZERO;
        quantity = 0;
        disabled = false;
        attachments = new ArrayList<>();
        tmpDiscounts = new ArrayList<>();

    }

    public Attachment getFeaturedImage() {
        List<Attachment> att = getAttachments();
        if (att != null && !att.isEmpty()) {
            return att.get(0);
        }
        return null;
    }

    public List<Attachment> getContentImages() {
        List<Attachment> att = getAttachments();
        if (att != null && !att.isEmpty()) {
            Attachment featuredImage = getFeaturedImage();
            if (featuredImage != null) {
                att.remove(featuredImage);
            }
            return att;
        }
        return null;
    }

    public BigDecimal getDiscountAmount() {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal finalPriceWithOutDiscount = getPrice();
        List<OrderItemDiscount> tmpDiscounts = getTmpDiscounts();
        if (tmpDiscounts != null && !tmpDiscounts.isEmpty()) {
            for (OrderItemDiscount disc : tmpDiscounts) {
                result = result.add(disc.getAbsoluteAmountDiscount(finalPriceWithOutDiscount));
            }
        }
        return result;
    }

    public BigDecimal getDiscountAmountInPercentage() {
        BigDecimal percentage = getDiscountAmount().divide(getPrice(), 6, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        if(percentage.compareTo(new BigDecimal(100)) > 0){
            percentage = new BigDecimal(100);
        }
        return percentage;
    }

    public BigDecimal getPriceWithDiscount() {
        BigDecimal amount = getPrice().subtract(getDiscountAmount());
        if(amount.compareTo(BigDecimal.ZERO) < 0){
            amount = BigDecimal.ZERO;
        }
        return amount;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public List<OrderItemDiscount> getTmpDiscounts() {
        return tmpDiscounts;
    }

    public void setTmpDiscounts(List<OrderItemDiscount> tmpDiscounts) {
        this.tmpDiscounts = tmpDiscounts;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.id.hashCode();
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
