/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers.interfaces;

import hr.workspace.models.ContactUser;
import hr.workspace.security.SecurityContext;
import javax.ejb.Local;

/**
 *
 * @author Stjepan
 */
@Local
public interface UserController {
    
    ContactUser newUser(SecurityContext sc);
    
    ContactUser saveUser(SecurityContext sc, ContactUser so);
    
    Boolean deleteUser(SecurityContext sc, ContactUser so);
    
}
