/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers.interfaces;

import hr.workspace.models.OrderItem;
import hr.workspace.models.Product;
import hr.workspace.models.UserOrder;
import hr.workspace.security.SecurityContext;
import javax.ejb.Remote;

/**
 *
 * @author Stjepan
 */
@Remote
public interface OrderController {
    
    UserOrder newOrder(SecurityContext sc);
    
    UserOrder save(SecurityContext sc, UserOrder so);
    
    Boolean deleteOrder(SecurityContext sc, UserOrder so);
    
    UserOrder addProductToOrder(SecurityContext sc, UserOrder order, Product product);
}
