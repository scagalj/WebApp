/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Stjepan
 */
@Entity
public class ACLUser implements IEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "acluser_id_seq")
    private Long id;

    private String userName;
    private String password;

    private Boolean isSuperUser;
    private Boolean disabled;

    public ACLUser() {

        userName = "";
        password = "";
        isSuperUser = false;
        disabled = false;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsSuperUser() {
        return isSuperUser;
    }

    public void setIsSuperUser(Boolean isSuperUser) {
        this.isSuperUser = isSuperUser;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

}
