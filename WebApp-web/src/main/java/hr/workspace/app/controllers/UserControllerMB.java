/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.app.controllers;

import hr.workspace.controllers.interfaces.UserController;
import hr.workspace.models.Attachment;
import hr.workspace.models.ContactUser;
import hr.workspace.models.Representative;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;
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
    private Representative representative;
    private ContactUser contact;
    private UploadedFiles attachments;

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
    
    public void addNewContact(){
        setContact(controller.newContact(getSecurityContext(), getUser()));
    }
    
    public void saveContact(){
        saveContact(getContact());
    }
    public void saveContact(ContactUser contact){
        setUser(controller.addContactToContactUser(getSecurityContext(), getUser(), contact));
        setContact(null);
    }
    
    public void deleteContact(){
        setUser(controller.removeContactFromContactUser(getSecurityContext(), getUser(), getContact()));
    }
    
    public Representative getRepresentative() {
        return representative;
    }

    public void setRepresentative(Representative representative) {
        this.representative = representative;
    }

    public ContactUser getContact() {
        return contact;
    }

    public void setContact(ContactUser contact) {
        this.contact = contact;
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

    public UploadedFiles getAttachments() {
        return attachments;
    }

    public void setAttachments(UploadedFiles attachments) {
        this.attachments = attachments;
    }
    
    
    
}
