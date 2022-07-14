/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers.interfaces;

import hr.workspace.models.Attachment;
import hr.workspace.models.ContactUser;
import hr.workspace.security.SecurityContext;
import javax.ejb.Local;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Stjepan
 */
@Local
public interface UserController {
    
    ContactUser newUser(SecurityContext sc, String email);
    
    ContactUser saveUser(SecurityContext sc, ContactUser so);
    
    Boolean deleteUser(SecurityContext sc, ContactUser so);
    
    ContactUser saveAttachmen(SecurityContext sc, ContactUser user, UploadedFile file);
    
    Boolean deleteAttachment(SecurityContext sc, ContactUser user, Attachment att);
    
}
