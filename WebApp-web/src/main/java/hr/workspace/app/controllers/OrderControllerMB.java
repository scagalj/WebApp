/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.app.controllers;

import hr.workspace.controllers.interfaces.OrderController;
import hr.workspace.controllers.interfaces.ProductCommons;
import hr.workspace.models.OrderItem;
import hr.workspace.models.Product;
import hr.workspace.models.UserOrder;
import hr.workspace.models.UserOrderStatus;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    
//    private UserOrder order;
    
    public UserOrder getOpenOrder(){
        List<UserOrder> orders = getUser().getOrders();
        for(UserOrder order : orders){
            if(UserOrderStatus.INIT.equals(order.getUserOrderStatus())){
                return order;
            }
        }
        Optional<UserOrder> activeOrder = orders.stream().filter(o -> UserOrderStatus.INIT.equals(o.getUserOrderStatus())).findFirst();
        if(activeOrder.isPresent()){
            UserOrder order = activeOrder.get();
            return order;
        }
        return null;
    }
    
    public void removeOrderItemFromOrder(OrderItem orderItem){
        UserOrder order = orderController.removeOrderItemFromOrder(getSecurityContext(), getOrder(), orderItem);
        setOrder(order);
    }
    
    public void addOrderItemToOrder(Product product){
        UserOrder order = orderController.addProductToOrder(getSecurityContext(), getOrder(), product);
        setOrder(order);
        addSuccessMessage(product.getName(), "Sucessfuly added to cart!");
    }

    
    protected void createNewOrderInternal() {
        UserOrder newOrder = orderController.newOrder(getSecurityContext(), getUser(), getSalesObject());
        newOrder = orderController.save(getSecurityContext(), newOrder);
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
    
    public UserOrder getOrder(){
        if(getSecurityContext().getOrder() == null){
            getSecurityContext().setOrder(getOpenOrder());
        }
        return getSecurityContext().getOrder();
    }

    public void setOrder(UserOrder order) {
        getSecurityContext().setOrder(order);
    }
    
    
}
