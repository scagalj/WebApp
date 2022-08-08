/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.controllers.interfaces.OrderCommons;
import hr.workspace.models.ContactUser;
import hr.workspace.models.OrderItem;
import hr.workspace.models.Product;
import hr.workspace.models.SalesObject;
import hr.workspace.models.UserOrder;
import hr.workspace.models.UserOrderStatus;
import hr.workspace.security.SecurityContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

/**
 *
 * @author Stjepan
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class OrderCommonsBean extends AbstractCommonsBean<UserOrder> implements OrderCommons{
    
    @Override
    public List<UserOrder> getAll(SecurityContext sc) {
        try {
            List<UserOrder> result = em.createNamedQuery(UserOrder.getAll).getResultList();
            return result;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    
    @Override
    public List<UserOrder> getAllActive(SecurityContext sc) {
        try {
            List<UserOrder> result = em.createNamedQuery(UserOrder.getAllActive).getResultList();
            return result;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    
    
    @Override
    public List<UserOrder> getCurrentlyActiveOrderForUser(SecurityContext sc, ContactUser user, SalesObject salesObject){
        System.out.println("TEST1");
        Query query = em.createQuery("Select u from UserOrder u "
                + "where u.contactUser = :contactUser "
                + "and u.salesObject = :salesObject "
                + "and (u.userOrderStatus = :init or u.userOrderStatus = :progress)");
        query.setParameter("contactUser", user);
        query.setParameter("salesObject", salesObject);
        query.setParameter("init", UserOrderStatus.INIT);
        query.setParameter("progress", UserOrderStatus.IN_PROGRESS);
//        Query q = em.createNativeQuery("Select u From UserOrder u where u.contactuser_id = #id and u.salesobject_id = #salesObjectId and (userorderstatus = 0 or userorderstatus = 1)");
        List<UserOrder> orders = new ArrayList<>(query.getResultList());
        return orders;
        
    }
    
    public List<UserOrder> getCompletedOrdersForSalesObject(SecurityContext sc, SalesObject salesObject){
        Query query = em.createQuery("Select u from UserOrder u "
                + "where u.salesObject = :salesObject "
                + "and (u.userOrderStatus = :init or u.userOrderStatus = :progress)");
        query.setParameter("salesObject", salesObject);
        query.setParameter("init", UserOrderStatus.COMPLETED);
        query.setParameter("progress", UserOrderStatus.AUTHORIZED);
        List<UserOrder> orders = new ArrayList<>(query.getResultList());
        return orders;
    }
    
    @Override
    public Map<Product,Integer> calculateAvailabeQuantityPerProduct(SecurityContext sc, List<Product> products, List<UserOrder> orders){
        Map<Product,Integer> result = new HashMap<>();
        List<OrderItem> orderItems  = orders.stream().flatMap(o -> o.getOrderItems().stream()).collect(Collectors.toList());
        for(OrderItem oi : orderItems){
            if(result.containsKey(oi.getProduct())){
                result.put(oi.getProduct(), result.get(oi.getProduct()) + oi.getQuantity());
            }else{
                result.put(oi.getProduct(), oi.getQuantity());
            }
        }
        
        for (Map.Entry<Product, Integer> entry : result.entrySet()) {
            Product key = entry.getKey();
            Integer val = entry.getValue();
            System.out.println("Product: " + key.getName() + " : Quantity : " + val);
        }
        return result;
    }
    
}
