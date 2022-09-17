/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.app.controllers;

import hr.workspace.app.commons.ActivityDisplay;
import hr.workspace.controllers.interfaces.UserController;
import hr.workspace.models.Attachment;
import hr.workspace.models.ContactUser;
import hr.workspace.models.Payment;
import hr.workspace.models.Representative;
import hr.workspace.models.UserOrder;
import hr.workspace.models.UserOrderStatus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Stjepan
 */
@Named(value = "UserControllerMB")
@ViewScoped
public class UserControllerMB extends BaseManagedBean {

    @EJB
    private UserController controller;
    private Representative representative;
    private ContactUser contact;
    private List<UploadedFile> attachments;

    public void saveUser() {
        setUser(controller.saveUser(getSecurityContext(), getUser()));
        getSecurityContext().setLogedUser(getUser());
    }

    public void addNewRepresentative() {
        Representative representative = controller.newRepresentative(getSecurityContext(), getUser());
        setRepresentative(representative);
    }

    public void saveRepresentative() {
        saveRepresentative(getRepresentative());
    }

    public void saveRepresentative(Representative rep) {
        ContactUser user = controller.addRepresentativeToContactUser(getSecurityContext(), getUser(), rep);
        setUser(user);
        setRepresentative(null);
    }

    public void onRowEdit(RowEditEvent<Representative> event) {
        Representative rep = event.getObject();
        if (rep != null) {
            saveRepresentative(rep);
            FacesMessage msg = new FacesMessage("Representative saved", String.valueOf(event.getObject().getFirstName()));
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }
    }

    public void onRowCancel(RowEditEvent<Representative> event) {
        FacesMessage msg = new FacesMessage("Representative Cancelled", String.valueOf(event.getObject().getFirstName()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void addNewContact() {
        setContact(controller.newContact(getSecurityContext(), getUser()));
    }

    public void saveContact() {
        saveContact(getContact());
    }

    public void saveContact(ContactUser contact) {
        setUser(controller.addContactToContactUser(getSecurityContext(), getUser(), contact));
        setContact(null);
    }

    public void deleteContact() {
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
        List<UploadedFile> attachments = getAttachments();

        if (attachments != null) {
            for (UploadedFile f : attachments) {
                controller.saveAttachmen(getSecurityContext(), getUser(), f);
                System.out.println("FILE: " + f.getFileName());
            }
            setAttachments(null);
        }
    }

    public void deleteAttachment(Attachment att) {
        Boolean deleteAttachment = controller.deleteAttachment(getSecurityContext(), getUser(), att);
        if (deleteAttachment) {
            System.out.println("Uspijesno izbrisano.");
        } else {
            System.out.println("NIje uspio izbrisati attachment.");
        }
    }

    public void handleFileUploadTextarea(FileUploadEvent event) {
        if (!getAttachments().contains(event.getFile())) {
            getAttachments().add(event.getFile());
        }
    }

    public String getAttachedFileNames() {
        StringBuilder sb = new StringBuilder();
        if (getAttachments().isEmpty()) {
            sb.append("Drag and drop documents here to upload");
        } else {
            for (UploadedFile uf : getAttachments()) {
                sb.append(uf.getFileName()).append(" uploaded\n");
            }
        }

        return sb.toString();
    }

    public List<ActivityDisplay> getAllActivities() {
        List<Payment> payments = getUser().getAllPaymentsForSalesObject(getSalesObject());
        List<ActivityDisplay> paymentActivities = payments.stream()
                .map(p -> new ActivityDisplay(p.getPaymentDate(), p.getAmount(), "Payment")).collect(Collectors.toList());

        List<UserOrder> orders = getUser().getOrdersForSalesObject(getSalesObject());
        List<ActivityDisplay> orderActivities = orders.stream().map(o -> new ActivityDisplay(new Date(), o.getFinalPrice(), "Order")).collect(Collectors.toList());
        paymentActivities.addAll(orderActivities);
        Collections.sort(paymentActivities);
        return paymentActivities;
    }
    
    public List<UploadedFile> getAttachments() {
        if (attachments == null) {
            attachments = new ArrayList<>();
        }
        return attachments;
    }

    public void setAttachments(List<UploadedFile> attachments) {
        this.attachments = attachments;
    }
}
