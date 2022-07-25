/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.app.controllers;

import hr.workspace.controllers.interfaces.OrderController;
import hr.workspace.models.UserOrder;
import hr.workspace.models.UserOrderStatus;
import java.util.List;
import java.util.Optional;
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
    
    private UserOrder order;
    
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
    
    public void createNewOrder(){
        UserOrder newOrder = orderController.newOrder(getSecurityContext(), getUser());
        newOrder = orderController.save(getSecurityContext(), newOrder);
        setOrder(newOrder);
    }
    
    public String getNumberOrItemsInOrder(){
        UserOrder order = getOrder();
        if(order != null){
            return Integer.toString(order.getOrderItems().size());
        }
        return "0";
    }
    
    public UserOrder getOrder(){
        if(order == null){
            order = getOpenOrder();
        }
        return order;
    }

    public void setOrder(UserOrder order) {
        this.order = order;
    }
    
    
}
