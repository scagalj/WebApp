/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers.interfaces;

import hr.workspace.models.ContactUser;
import hr.workspace.security.SecurityContext;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Stjepan
 */
@Local
public interface UserCommons {
 
    List<ContactUser> getAllActive(SecurityContext sc);
    
    List<ContactUser> getAll(SecurityContext sc);
    
    ContactUser fetchUserByUUID(SecurityContext sc, String uuid);
    
    void saveFileToResource();
    
}
