/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.webapp.admin.controller;

import hr.workspace.controllers.interfaces.UserCommons;
import hr.workspace.controllers.interfaces.UserController;
import hr.workspace.models.Attachment;
import hr.workspace.models.ContactUser;
import java.util.UUID;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFiles;

/**
 *
 * @author Stjepan
 */
@Named(value = "UserControllerMB")
@ViewScoped
public class UserControllerMB extends BaseManagedBean{
    
    @EJB
    private UserController controller;
    @EJB
    private UserCommons commons;
    private ContactUser user;
    private UploadedFiles attachments;
    
    public void createNewUser(){
        ContactUser tmpUser = controller.newUser(getSecurityContext(), "admin@webapp.com");
        if(tmpUser != null){
            setUser(tmpUser);
                showDialog("newUserDialogWidget");
        }
    }
    
    public void editUser(ContactUser tmpUser){
        if(tmpUser != null){
            setUser(tmpUser);
            showDialog("newUserDialogWidget");
        }
    }

    public void saveUser(){
        if(getUser() != null){
            System.out.println("TEST1");
            if(getUser().getId() == null){
                
                String uniqueId = UUID.nameUUIDFromBytes(getUser().getEmail().getBytes()).toString();
            ContactUser existUser = commons.fetchUserByUUID(getSecurityContext(), uniqueId);
            if(existUser != null){
                System.out.println("USER WITH THIS EMAIL ALREADY EXIST!!!");
                return;
            }
            }
            ContactUser tmpUser = controller.saveUser(getSecurityContext(), getUser());
            if(tmpUser != null){
//                hideDialog("newUserDialogWidget");
                setUser(tmpUser);
            }
        }
    }
    
    public void deleteUser(ContactUser tmpUser){
        if(tmpUser != null){
            Boolean isObjectDeleted = controller.deleteUser(getSecurityContext(), tmpUser);
        }
    }
    
    public void uploadMultiple() {
        if (getAttachments() != null) {
            for (UploadedFile f : getAttachments().getFiles()) {
                controller.saveAttachmen(getSecurityContext(),getUser(), f);
                System.out.println("FILE: " + f.getFileName());
            }
            setAttachments(null);
        }
    }
    
    public void deleteAttachment(Attachment att){
        Boolean deleteAttachment = controller.deleteAttachment(getSecurityContext(), getUser(), att);
        if(deleteAttachment){
            System.out.println("Uspijesno izbrisano.");
        }else{
            System.out.println("NIje uspio izbrisati attachment.");
        }
    }

    public ContactUser getUser() {
        return user;
    }

    public void setUser(ContactUser user) {
        this.user = user;
    }

    public UploadedFiles getAttachments() {
        return attachments;
    }

    public void setAttachments(UploadedFiles attachments) {
        this.attachments = attachments;
    }
    
}
