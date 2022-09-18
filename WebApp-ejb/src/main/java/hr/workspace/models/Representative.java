/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.models;

import java.io.Serializable;
import java.util.Objects;
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
public class Representative implements IEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "representative_id_seq")
    private Long id;
    private String firstName;
    private String lastName;
    private Boolean ceo;
    @ManyToOne
    private ContactUser contactUser;

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

    public Boolean getCeo() {
        return ceo;
    }

    public void setCeo(Boolean ceo) {
        this.ceo = ceo;
    }

    public ContactUser getContactUser() {
        return contactUser;
    }

    public void setContactUser(ContactUser contactUser) {
        this.contactUser = contactUser;
    }

    public String getName() {
        String name = "";
        if (getFirstName() != null) {
            name += getFirstName() + " ";
        }
        if (getLastName() != null) {
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
        Representative other = (Representative) object;
        if (this.getId() == null && other.getId() == null) {
            return super.equals(other);
        }
        if (!Objects.equals(this.id, other.id) && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
