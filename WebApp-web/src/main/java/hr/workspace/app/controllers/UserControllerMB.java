/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.app.controllers;

import hr.workspace.controllers.interfaces.UserController;
import hr.workspace.models.ContactUser;
import hr.workspace.models.Representative;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Stjepan
 */
@Named(value = "UserControllerMB")
@ViewScoped
public class UserControllerMB extends BaseManagedBean{
    
    @EJB
    private UserController controller;
    private Representative representative;

    public void saveUser(){
        setUser(controller.saveUser(getSecurityContext(), getUser()));
        getSecurityContext().setLogedUser(getUser());
    }

    public void addNewRepresentative(){
        Representative representative = controller.newRepresentative(getSecurityContext(), getUser());
        setRepresentative(representative);
    }
    
    public void saveRepresentative(){
        saveRepresentative(getRepresentative());
    }
    public void saveRepresentative(Representative rep){
        ContactUser user = controller.addRepresentativeToContactUser(getSecurityContext(), getUser(), rep);
        setUser(user);
        setRepresentative(null);
    }
    
    public void onRowEdit(RowEditEvent<Representative> event) {
        Representative rep = event.getObject();
        if(rep != null){
            saveRepresentative(rep);
            FacesMessage msg = new FacesMessage("Representative saved", String.valueOf(event.getObject().getFirstName()));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        
        }
    }

    public void onRowCancel(RowEditEvent<Representative> event) {
        FacesMessage msg = new FacesMessage("Representative Cancelled", String.valueOf(event.getObject().getFirstName()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    
    public ContactUser getUser() {
        return getSecurityContext().getLogedUser();
    }

    public void setUser(ContactUser user) {
        getSecurityContext().setLogedUser(user);
    }

    public Representative getRepresentative() {
        return representative;
    }

    public void setRepresentative(Representative representative) {
        this.representative = representative;
    }
    
    
    
    
    
}
