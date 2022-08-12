/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.models;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Stjepan
 */
@Entity
public class OrderRepresentative implements IEntity, Serializable, Comparable<OrderRepresentative> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "orderrepresentative_id_seq")
    private Long id;

    private BigDecimal price;

    @ManyToOne
    private Representative representative;

    @ManyToOne
    private UserOrder userOrder;

//    private List<Extras> extras;
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Representative getRepresentative() {
        return representative;
    }

    public void setRepresentative(Representative representative) {
        this.representative = representative;
    }

    public UserOrder getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(UserOrder userOrder) {
        this.userOrder = userOrder;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
        OrderRepresentative other = (OrderRepresentative) object;
        if (this.getId() == null && other.getId() == null) {
            return super.equals(other);
        }
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(OrderRepresentative o) {
        if (o.getRepresentative() != null && this.getRepresentative() != null) {
            int firstName = this.getRepresentative().getFirstName().compareTo(o.getRepresentative().getFirstName());
            if (firstName != 0) {
                return firstName;
            }
            int lastName = this.getRepresentative().getLastName().compareTo(o.getRepresentative().getLastName());
            if (lastName != 0) {
                return lastName;
            }
        }

        return this.getId().compareTo(o.getId());
    }

}
