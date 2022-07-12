/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.security;

import hr.workspace.models.ACLUser;
import hr.workspace.models.ContactUser;
import java.io.Serializable;

/**
 *
 * @author Stjepan
 */
public class SecurityContext implements Serializable{
    
    private ContactUser logedUser;
    private ACLUser loggedACLUser;

    public ContactUser getLogedUser() {
        return logedUser;
    }

    public void setLogedUser(ContactUser logedUser) {
        this.logedUser = logedUser;
    }

    public ACLUser getLoggedACLUser() {
        return loggedACLUser;
    }

    public void setLoggedACLUser(ACLUser loggedACLUser) {
        this.loggedACLUser = loggedACLUser;
    }
    
    
    
    
}
