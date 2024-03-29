/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers.interfaces;

import hr.workspace.models.ContactUser;
import hr.workspace.models.Product;
import hr.workspace.models.SalesObject;
import hr.workspace.models.UserOrder;
import hr.workspace.security.SecurityContext;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author Stjepan
 */
@Local
public interface OrderCommons {
    
    List<UserOrder> getAll(SecurityContext sc);
    
    List<UserOrder> getAllActive(SecurityContext sc);
    
    List<UserOrder> getCurrentlyActiveOrderForUser(SecurityContext sc, ContactUser user, SalesObject salesObject);
    
    List<UserOrder> getCompletedOrdersForSalesObject(SecurityContext sc, SalesObject salesObject);
    
    List<UserOrder> getAllOrdersForSalesObjectForUser(SecurityContext sc,ContactUser user, SalesObject salesObject);
    
    Map<Product,Integer> calculateAvailabeQuantityPerProduct(SecurityContext sc, List<Product> products, List<UserOrder> orders);
}