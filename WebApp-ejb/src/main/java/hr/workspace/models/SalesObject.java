/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author Stjepan
 */
@Entity
@NamedQueries({
    @NamedQuery(name = SalesObject.getAll, query = "select so from SalesObject so order by so.salesObjectName"),
    @NamedQuery(name = SalesObject.getAllActive, query = "select so from SalesObject so where so.disabled=false order by so.salesObjectName")
})
public class SalesObject implements IEntity, Serializable{
    
    public final static String getAll = "hr.workspace.models.SalesObject.getAll";
    public final static String getAllActive = "hr.workspace.models.SalesObject.getAllActive";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "salesobject_id_seq")
    private Long id;
    
//    @OneToMany(mappedBy = "salesObject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<User> contacts;
//    
//    @OneToMany(mappedBy = "salesObject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<ACLUser> users;
    
    @OneToMany(mappedBy = "salesObject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;
    
    @OneToMany(mappedBy = "salesObject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserOrder> orders;
    
    private String salesObjectName;
    private Boolean disabled;

    public SalesObject() {
        disabled = false;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public List<User> getContacts() {
//        return contacts;
//    }
//
//    public void setContacts(List<User> contacts) {
//        this.contacts = contacts;
//    }
//
//    public List<ACLUser> getUsers() {
//        return users;
//    }
//
//    public void setUsers(List<ACLUser> users) {
//        this.users = users;
//    }

    public List<UserOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<UserOrder> orders) {
        this.orders = orders;
    }

    
    
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getSalesObjectName() {
        return salesObjectName;
    }

    public void setSalesObjectName(String salesObjectName) {
        this.salesObjectName = salesObjectName;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
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
        SalesObject other = (SalesObject) object;
        if (this.getId() == null && other.getId() == null) {
            return super.equals(other);
        }
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    
    
}
