/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.app.controllers;

import hr.workspace.controllers.interfaces.OrderCommons;
import hr.workspace.controllers.interfaces.OrderController;
import hr.workspace.controllers.interfaces.ProductCommons;
import hr.workspace.models.OrderItem;
import hr.workspace.models.Product;
import hr.workspace.models.UserOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Stjepan
 */
@Named(value = "OrderControllerMB")
@ViewScoped
public class OrderControllerMB extends BaseManagedBean{
    
    @EJB
    OrderController orderController;
    @EJB
    ProductCommons productCommons;
    @EJB
    OrderCommons orderCommons;
    private List<UserOrder> orders;
    List<OrderItem> orderItems;
    
//    private UserOrder order;
    
    @PostConstruct
    public void init() {
            System.out.println("TEST MAKE ORDER INIT");
        if (getOrder() == null) {
            List<UserOrder> orders = orderCommons.getCurrentlyActiveOrderForUser(getSecurityContext(), getUser(), getSalesObject());
            if (orders != null && !orders.isEmpty()) {
                setOrder(orders.get(0));
            }
        }
        
        if (orderItems == null) {
            orderItems = new ArrayList<>();
            for (UserOrder order : getSortedUserOrders()) {
                orderItems.addAll(order.getOrderItems());
            }
            Collections.sort(orderItems, new Comparator<OrderItem>() {
                @Override
                public int compare(OrderItem o1, OrderItem o2) {
                    return o2.getUserOrder().getId().compareTo(o1.getUserOrder().getId());
                }
            } );
        System.out.println("ORDERS:");
        orderItems.forEach(o1 -> System.out.println(o1.getUserOrder().getId()));
        }
        
    }
    
    public void removeOrderItemFromOrder(OrderItem orderItem){
        UserOrder order = orderController.removeOrderItemFromOrder(getSecurityContext(), getOrder(), orderItem, getUser());
        setOrder(order);
    }
    
    public void addOrderItemToOrder(Product product){
        UserOrder order = orderController.addProductToOrder(getSecurityContext(), getOrder(), product, getUser());
        setOrder(order);
        addSuccessMessage(product.getName(), "Sucessfuly added to cart!");
    }

    public void makeOrderAsCompleted(){
        UserOrder order = orderController.makeOrderAsCompleted(getSecurityContext(), getOrder(), getUser());
        if(order != null){
            setOrder(null);
            addSuccessMessage("Order completed");
        }
    }
    
    
    
    public void makeOrderAsCancelled(){
        UserOrder order = orderController.makeOrderAsCancelled(getSecurityContext(), getOrder(), getUser());
        if(order != null){
            setOrder(null);
            addSuccessMessage("Order canceled");
        }
    }
    
    protected void createNewOrderInternal() {
        UserOrder newOrder = orderController.newOrder(getSecurityContext(), getUser(), getSalesObject());
        newOrder = orderController.save(getSecurityContext(), newOrder, getUser());
        setOrder(newOrder);
    }
    
    public void createNewOrder() {
        createNewOrderInternal();
        navigate("order.xhtml?faces-redirect=true");
    }
    
    public String getNumberOrItemsInOrder(){
        UserOrder order = getOrder();
        if(order != null){
            return Integer.toString(order.getOrderItems().size());
        }
        return "0";
    }
    
    public List<Product> getAllActiveProducts(){
        List<Product> products = productCommons.getAllForSalesObject(getSecurityContext(), getSalesObject());
        products = products.stream().filter(p -> !p.getDisabled()).collect(Collectors.toList());
        return products;
    }
    
    public List<UserOrder> getSortedUserOrders() {
        if(orders == null){
            orders = getUser().getOrders();
            Collections.sort(orders);
        }
        return orders; 
    }
    
    public List<OrderItem> getUserOrderItems() {
        
        return orderItems;
    }
    
    public UserOrder getOrder(){
        return getSecurityContext().getOrder();
    }

    public void setOrder(UserOrder order) {
        getSecurityContext().setOrder(order);
    }
    
    
}
