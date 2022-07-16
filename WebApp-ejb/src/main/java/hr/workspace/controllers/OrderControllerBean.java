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
import hr.workspace.models.Product;
import hr.workspace.models.UserOrder;
import hr.workspace.security.SecurityContext;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    public UserOrder save(SecurityContext sc, UserOrder so) {
        try {
            utx.begin();
            for(OrderItem oi : so.getOrderItems()){
                merge(oi);
            }
            
            UserOrder salesObject = super.save(sc, so);
            return salesObject;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return so;
    }

    @Override
    public Boolean deleteOrder(SecurityContext sc, UserOrder userOrder) {
        try {
            List<OrderItem> orderItems = userOrder.getOrderItems();
            int size = orderItems.size();
            for (OrderItem oi : new ArrayList<>(orderItems)) {
                userOrder = removeOrderItemFromOrder(sc, userOrder, oi);
            }

            userOrder = merge(userOrder);
            Boolean success = super.delete(sc, userOrder);
            return success;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return false;
    }

    @Override
    public UserOrder removeOrderItemFromOrder(SecurityContext sc, UserOrder order, OrderItem orderItem) {
        try {

            utx.begin();
            orderItem.setUserOrder(null);
            orderItem.setProduct(null);
            order.getOrderItems().remove(orderItem);
            boolean result = super.remove(orderItem);
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
    public UserOrder addProductToOrder(SecurityContext sc, UserOrder order, Product product) {
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
}
