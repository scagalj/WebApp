/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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
    @NamedQuery(name = ContactUser.getAll, query = "select u from ContactUser u order by u.firstName"),
    @NamedQuery(name = ContactUser.getAllActive, query = "select u from ContactUser u where u.disabled=false order by u.firstName")
})
public class ContactUser implements IEntity, Serializable {

    public final static String getAll = "hr.workspace.models.ContactUser.getAll";
    public final static String getAllActive = "hr.workspace.models.ContactUser.getAllActive";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "contactuser_id_seq")
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    
    private String uuid;
    private String uniqueId;

    private Boolean disabled;

    @OneToMany(mappedBy = "contactUser", fetch = FetchType.LAZY)
    private List<UserOrder> orders;

    public ContactUser() {
    }
    
    public ContactUser(String email) {
        this.firstName = "";
        this.lastName = "";
        this.email = email;
        this.disabled = false;
        this.orders = new ArrayList<>();
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    public List<UserOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<UserOrder> orders) {
        this.orders = orders;
    }
    
    public String getName(){
        String name = "";
        if(getFirstName() != null){
            name += getFirstName() + " ";
        }
        if(getLastName() != null){
            name += getLastName() + " ";
        }
        
        return name;
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
        ContactUser other = (ContactUser) object;
        if (this.getId() == null && other.getId() == null) {
            return super.equals(other);
        }
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
    
}
