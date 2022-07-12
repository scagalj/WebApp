/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.webapp.admin.controller;

import hr.workspace.controllers.interfaces.UserController;
import hr.workspace.models.ContactUser;
import hr.workspace.models.ContactUser;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Stjepan
 */
@Named(value = "UserControllerMB")
@ViewScoped
public class UserControllerMB extends BaseManagedBean{
    
    @EJB
    private UserController controller;
    private ContactUser user;
    
    public void createNewUser(){
        ContactUser tmpUser = controller.newUser(getSecurityContext());
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
            ContactUser tmpUser = controller.saveUser(getSecurityContext(), getUser());
            if(tmpUser != null){
                hideDialog("newUserDialogWidget");
                setUser(null);
            }
        }
    }
    
    public void deleteUser(ContactUser tmpUser){
        if(tmpUser != null){
            Boolean isObjectDeleted = controller.deleteUser(getSecurityContext(), tmpUser);
        }
    }

    public ContactUser getUser() {
        return user;
    }

    public void setUser(ContactUser user) {
        this.user = user;
    }
    
}
