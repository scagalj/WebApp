/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers.interfaces;

import hr.workspace.models.Attachment;
import hr.workspace.models.ContactUser;
import hr.workspace.models.OrderItem;
import hr.workspace.models.Payment;
import hr.workspace.models.Product;
import hr.workspace.models.UserOrder;
import hr.workspace.security.SecurityContext;
import javax.ejb.Local;
import javax.ejb.Remote;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Stjepan
 */
@Local
public interface OrderController {
    
    UserOrder newOrder(SecurityContext sc);
    
    UserOrder newOrder(SecurityContext sc, ContactUser user);
    
    UserOrder save(SecurityContext sc, UserOrder so);
    
    Boolean deleteOrder(SecurityContext sc, UserOrder so);
    
    UserOrder addProductToOrder(SecurityContext sc, UserOrder order, Product product);

    UserOrder removeOrderItemFromOrder(SecurityContext sc, UserOrder order, OrderItem orderItem);
    
    Boolean deleteAttachment(SecurityContext sc, UserOrder order, Attachment att);
    
    UserOrder saveAttachmen(SecurityContext sc, UserOrder order, UploadedFile file);
    
    UserOrder addPaymentToOrder(SecurityContext sc, UserOrder order, Payment payment);
    
    UserOrder removePaymentFromOrder(SecurityContext sc, UserOrder order, Payment payment);
    
    Payment newPayment(SecurityContext sc, UserOrder order);
}
