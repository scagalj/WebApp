/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.common.FileUtils;
import hr.workspace.controllers.interfaces.OrderCommons;
import hr.workspace.controllers.interfaces.OrderController;
import hr.workspace.models.Attachment;
import hr.workspace.models.ContactUser;
import hr.workspace.models.OrderItem;
import hr.workspace.models.Payment;
import hr.workspace.models.Product;
import hr.workspace.models.SalesObject;
import hr.workspace.models.UserOrder;
import hr.workspace.models.UserOrderStatus;
import hr.workspace.security.SecurityContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.criteria.Order;
import javax.transaction.SystemException;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Stjepan
 */
@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class OrderControllerBean extends MainAdminTransactionControllerBean<UserOrder> implements OrderController {

    @EJB
    private OrderCommons commons;

    @Deprecated
    @Override
    public UserOrder newOrder(SecurityContext sc) {
        try {
            UserOrder result = new UserOrder();
            return result;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return null;
    }
    
    @Override
    public UserOrder newOrder(SecurityContext sc, ContactUser user, SalesObject salesObject) {
        try {
            UserOrder result = new UserOrder();
            result.setSalesObject(salesObject);
            result.setContactUser(user);
            return result;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return null;
    }

    @Deprecated
    @Override
    public UserOrder save(SecurityContext sc, UserOrder so) {
        try {
            utx.begin();
            for(OrderItem oi : so.getOrderItems()){
                merge(oi);
            }
            
            UserOrder userOrder = super.save(sc, so);
            return userOrder;
        } catch (Exception ex) {
                        try {
                utx.rollback();
            } catch (Exception ex1) {
                log(sc, Level.ALL, ex1, true);
            }
            log(sc, Level.ALL, ex, true);
        }
        return so;
    }
    
    @Override
    public UserOrder save(SecurityContext sc, UserOrder userOrder, ContactUser user) {
        try {
            utx.begin();
            for(OrderItem oi : userOrder.getOrderItems()){
                merge(oi);
            }
            userOrder = super.save(sc, userOrder);
            
            utx.begin();
            if(user != null){
                if(!user.getOrders().contains(userOrder)){
                    user.getOrders().add(userOrder);
                }
                merge(user);
            }
            
            utx.commit();
            
            return userOrder;
        } catch (Exception ex) {
                        try {
                utx.rollback();
            } catch (Exception ex1) {
                log(sc, Level.ALL, ex1, true);
            }
            log(sc, Level.ALL, ex, true);
        }
        return userOrder;
    }
    
    @Override
    public UserOrder makeOrderAsCompleted(SecurityContext sc, UserOrder order, ContactUser user){
        try{
            utx.begin();
            order.setUserOrderStatus(UserOrderStatus.COMPLETED);
            order = merge(order);
            
            if(user != null && order != null){
                if(user.getOrders().contains(order)){
                    user.getOrders().remove(order);
                }
                user.getOrders().add(order);
                merge(user);
            }
            
            utx.commit();
            return order;
        }catch(Exception ex){
            try {
                utx.rollback();
            } catch (Exception ex1) {
                log(sc, Level.ALL, ex1, true);
            }
            log(sc, Level.ALL, ex, true);
        }
        return order;
    }
    
    @Override
    public UserOrder makeOrderAsCancelled(SecurityContext sc, UserOrder order, ContactUser user){
        try{
            utx.begin();
            order.setUserOrderStatus(UserOrderStatus.CANCELLED);
            order = merge(order);
            
            if(user != null && order != null){
                if(user.getOrders().contains(order)){
                    user.getOrders().remove(order);
                }
                user.getOrders().add(order);
                merge(user);
            }
            
            utx.commit();
            return order;
        }catch(Exception ex){
            try {
                utx.rollback();
            } catch (Exception ex1) {
                log(sc, Level.ALL, ex1, true);
            }
            log(sc, Level.ALL, ex, true);
        }
        return order;
    }

    @Override
    public Boolean deleteOrder(SecurityContext sc, UserOrder userOrder) {
        try {
            List<OrderItem> orderItems = userOrder.getOrderItems();
            int size = orderItems.size();
            for (OrderItem oi : new ArrayList<>(orderItems)) {
                userOrder = removeOrderItemFromOrder(sc, userOrder, oi, userOrder.getUser());
            }
            
            //TODO izbrisati Attachment i Paymente!!!!!
            //TODO izbrisati Attachment i Paymente!!!!!
            //TODO izbrisati Attachment i Paymente!!!!!
            //TODO izbrisati Attachment i Paymente!!!!!
            //TODO izbrisati Attachment i Paymente!!!!!
            //TODO izbrisati Attachment i Paymente!!!!!

            userOrder = merge(userOrder);
            Boolean success = super.delete(sc, userOrder);
            return success;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return false;
    }

    @Override
    public UserOrder removeOrderItemFromOrder(SecurityContext sc, UserOrder order, OrderItem orderItem, ContactUser user) {
        try {

            utx.begin();
            orderItem.setUserOrder(null);
            orderItem.setProduct(null);
            order.getOrderItems().remove(orderItem);
            boolean result = super.remove(orderItem);
            order = merge(order);
            
            if (user != null) {
                if (user.getOrders().contains(order)) {
                    user.getOrders().remove(order);
                }
                user.getOrders().add(order);
                merge(user);
            }
            
            if (result) {
                utx.commit();
                return order;
            } else {
                utx.rollback();
            }
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception ex1) {
                log(sc, Level.ALL, ex1, true);
            }
            log(sc, Level.ALL, ex, true);
        }
        return order;
    }

    @Override
    public UserOrder addProductToOrder(SecurityContext sc, UserOrder order, Product product, ContactUser user) {
        try {
            System.out.println("TEST1");
            utx.begin();

            OrderItem orderItem = null;
            List<OrderItem> orderItems = order.getOrderItems();
            Optional<OrderItem> optionalOrderItem = orderItems.stream().filter(oi -> oi.getProduct().getId().equals(product.getId())).findFirst();
            if (optionalOrderItem.isPresent()) {
                OrderItem tmorderItem = optionalOrderItem.get();
                tmorderItem.setQuantity(tmorderItem.getQuantity() + 1);
                order.getOrderItems().remove(tmorderItem);
                order.getOrderItems().add(tmorderItem);

                merge(tmorderItem);

            } else {
                orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setUserOrder(order);
                orderItem.setPrice(product.getPrice());
                orderItem.setDiscount(BigDecimal.ZERO);
                order.getOrderItems().add(orderItem);
                persist(orderItem);
            }
            
            order.setUserOrderStatus(UserOrderStatus.IN_PROGRESS);
            order = merge(order);
            
            if (user != null) {
                if (user.getOrders().contains(order)) {
                    user.getOrders().remove(order);
                }
                user.getOrders().add(order);
                merge(user);
            }
            
            utx.commit();
            return order;
        } catch (Exception ex) {
            log(sc, Level.SEVERE, ex, true);
            try {
                utx.rollback();
            } catch (Exception ex1) {
                log(sc, Level.SEVERE, ex1, true);
            }
        }
        return null;
    }
    
    
    @Override
    public UserOrder saveAttachmen(SecurityContext sc, UserOrder order, UploadedFile file) {
        if (file.getSize() <= 0) {
            return null;
        }
        Attachment att = new Attachment();
        att.setContentType(file.getContentType());
        att.setFileName(file.getFileName());
        att.setInternalName(file.getFileName() + "_" + UUID.randomUUID().toString());
        att.setDataSize(file.getSize());
        att.setFileDescription("Radnom tekst za sad");
        att.setData(file.getContent());
        att.setUserOrder(order);
        Boolean isFileSaved = FileUtils.saveFileToDisk(att);
        if (isFileSaved) {
            try {
                utx.begin();
                em.persist(att);
                order.getAttachments().add(att);
                order = merge(order);
                utx.commit();
                System.out.println("FILE uspjesno spremljen na disk!!!");
            } catch (Exception ex) {
                try {
                    utx.rollback();
                } catch (Exception ex1) {
                    log(sc, Level.SEVERE, ex1, true);
                }
                log(sc, Level.SEVERE, ex, true);
            }
        } else {
            System.out.println("FILE nije spremljen na disk!!!");
        }
        return order;

    }

    @Override
    public Boolean deleteAttachment(SecurityContext sc, UserOrder order, Attachment att) {
        try {
            Boolean isDeleted = FileUtils.deleteFileFromDisk(att);
            if (isDeleted) {
                order.getAttachments().remove(att);
                utx.begin();
                if (!em.contains(att)) {
                    att = em.merge(att);
                }
                em.remove(att);
                utx.commit();
                return true;
            }
        } catch (Exception e) {
            log(sc, Level.SEVERE, e, true);
        }
        return false;
    }
    
    @Override
    public Payment newPayment(SecurityContext sc, UserOrder order){
        Payment payment = new Payment();
        payment.setUserOrder(order);
        payment.setPaymentDate(new Date());
        return payment;
    }
    
    @Override
    public UserOrder removePaymentFromOrder(SecurityContext sc, UserOrder order, Payment payment) {
        try {

            utx.begin();
            payment.setUserOrder(null);
            if(order.getPayments().contains(payment)){
                order.getPayments().remove(payment);
            }
            boolean result = super.remove(payment);
            order = merge(order);
            if (result) {
                utx.commit();
                return order;
            } else {
                utx.rollback();
            }
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception ex1) {
                log(sc, Level.ALL, ex1, true);
            }
            log(sc, Level.ALL, ex, true);
        }
        return order;
    }

    @Override
    public UserOrder addPaymentToOrder(SecurityContext sc, UserOrder order, Payment payment) {
        try {
            System.out.println("TEST ADD PAYMENT START");
            utx.begin();
            if(!order.getPayments().contains(payment)){
                order.getPayments().add(payment);
            }
            if(payment.getId() == null){
                persist(payment);
            }else{
                merge(payment);
            }
            order = merge(order);
            utx.commit();
            System.out.println("TEST ADD PAYMENT END");
            return order;
        } catch (Exception ex) {
            log(sc, Level.SEVERE, ex, true);
            try {
                utx.rollback();
            } catch (Exception ex1) {
                log(sc, Level.SEVERE, ex1, true);
            }
        }
        return null;
    }
    
    public Boolean canCreateNewOrder(SecurityContext sc, ContactUser user, SalesObject salesObject){
        //TODO: Provjera postoji li vec kreirana narduzba koja je u initu ili progresu!
        return true;
    }
}
