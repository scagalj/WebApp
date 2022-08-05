/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.webapp.admin.controller;

import hr.workspace.controllers.interfaces.DiscountCommons;
import hr.workspace.controllers.interfaces.DiscountController;
import hr.workspace.models.ContactUser;
import hr.workspace.models.Discount;
import hr.workspace.models.DiscountType;
import hr.workspace.models.Product;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Stjepan
 */
@Named(value = "DiscountControllerMB")
@ViewScoped
public class DiscountControllerMB extends BaseManagedBean {

    private final static String DISCOUNT_DIALOG_NAME = "newDiscountDialogWidget";

    @EJB
    private DiscountController controller;
    @EJB
    private DiscountCommons commons;
    private Discount discount;
    private ContactUser user;
    private Product product;
//    private UploadedFiles attachments;

    public void createNewDiscount() {
        Discount tmpDiscount = controller.newDiscount(getSecurityContext());
        if (tmpDiscount != null) {
            setDiscount(tmpDiscount);
            showDialog(DISCOUNT_DIALOG_NAME);
        }
    }

    public void editDiscount(Discount tmpDiscount) {
        if (tmpDiscount != null) {
            setDiscount(tmpDiscount);
            showDialog(DISCOUNT_DIALOG_NAME);
        }
    }

    public void closeDiscount() {
        hideDialog(DISCOUNT_DIALOG_NAME);
        setDiscount(null);
    }

    public void saveDiscount() {
        if (getDiscount() != null) {
            System.out.println("TEST1");

            Discount tmpDiscount = controller.saveDiscount(getSecurityContext(), getDiscount());
            if (tmpDiscount != null) {
//                hideDialog("newUserDialogWidget");
                setDiscount(tmpDiscount);
            }
        }
    }

    public void deleteDiscount(Discount tmpDiscount) {
        if (tmpDiscount != null) {
            Boolean isObjectDeleted = controller.deleteDiscount(getSecurityContext(), tmpDiscount);
        }
    }

    public void onContactUserSelectAutocomplete(SelectEvent<ContactUser> event) {
        if (event.getObject() != null) {
            ContactUser contactUser = event.getObject();
            System.out.println("ITEM SELECTED: " + contactUser.getName());
            addContactUserToDiscount(contactUser);
        }
    }
    
    public void addContactUserToDiscount(ContactUser user) {
        Discount discount = controller.addContactUser(getSecurityContext(), getDiscount(), user);
        setDiscount(discount);
        setUser(null);
    }
    
    public void onProductSelectAutocomplete(SelectEvent<Product> event) {
        if (event.getObject() != null) {
            Product product = event.getObject();
            System.out.println("ITEM SELECTED: " + product.getName());
            addProductToDiscount(product);
        }
    }

    public void addProductToDiscount(Product product) {
        Discount discount = controller.addProduct(getSecurityContext(), getDiscount(), product);
        setDiscount(discount);
        setProduct(null);
    }

    public void deleteContactUserFromDiscont(ContactUser user) {
        Discount discount = controller.removeContactUser(getSecurityContext(), getDiscount(), user);
        setDiscount(discount);
    }

    public void deleteProductFromDiscont(Product product) {
        Discount discount = controller.removeProduct(getSecurityContext(), getDiscount(), product);
        setDiscount(discount);
    }
    
    public List<SelectItem> getAllDiscountTypes(){
        List<SelectItem> result = new ArrayList<>();
        
        for(DiscountType type : DiscountType.values()){
            result.add(new SelectItem(type, type.name()));
        }
        
        return result;
        
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public ContactUser getUser() {
        return user;
    }

    public void setUser(ContactUser user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
