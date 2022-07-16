/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers.interfaces;

import hr.workspace.models.Attachment;
import hr.workspace.models.ContactUser;
import hr.workspace.models.Discount;
import hr.workspace.models.Product;
import hr.workspace.security.SecurityContext;
import javax.ejb.Local;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Stjepan
 */
@Local
public interface DiscountController {
    
    Discount newDiscount(SecurityContext sc);
    
    Discount saveDiscount(SecurityContext sc, Discount discount);
    
    Boolean deleteDiscount(SecurityContext sc, Discount discount);
    
    Discount addContactUser(SecurityContext sc, Discount discount, ContactUser user);
    
    Discount removeContactUser(SecurityContext sc, Discount discount, ContactUser user);
    
    Discount addProduct(SecurityContext sc, Discount discount, Product product);
    
    Discount removeProduct(SecurityContext sc, Discount discount, Product product);
    
}
