/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.app.controllers;

import hr.workspace.controllers.interfaces.OrderController;
import hr.workspace.models.OrderItem;
import hr.workspace.models.UserOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.component.spinner.Spinner;

/**
 *
 * @author Stjepan
 */
@Named(value = "CheckOutControllerMB")
@ViewScoped
public class CheckOutControllerMB extends BaseManagedBean{

    @EJB
    private OrderController orderController;
    
    private List<OrderItem> orderItems = new ArrayList<>();
    private OrderItem selectedOrderItem;

    @PostConstruct
    public void init(){
        List<OrderItem> result = getOrder().getOrderItems();
        Collections.sort(result, new Comparator<OrderItem>() {
            @Override
            public int compare(OrderItem o1, OrderItem o2) {
                return o1.getProduct().getProductType().compareTo(o2.getProduct().getProductType());
            }
        });
        orderItems = new ArrayList<>(result);
    }
    
    public List<OrderItem> getOrderItems(){
//        return getOrder().getOrderItems();
    return orderItems;
}
    
    public void updateOrderItemQuantity(AjaxBehaviorEvent event) {
        OrderItem orderItem = (OrderItem) event.getComponent().getAttributes().get("selectedRow");
        Integer newValue = (Integer) ((Spinner) event.getSource()).getValue();
        if (orderItem != null && newValue != null) {
            UserOrder updatedOrder = orderController.updateOrderItemQuantity(getSecurityContext(), getOrder(), getUser(), orderItem, newValue);
            if (updatedOrder != null) {
                setOrder(updatedOrder);
            }
        } else {
            addErrorMessage("Can't update quantity");
        }
    }

    public OrderItem getSelectedOrderItem() {
        return selectedOrderItem;
    }

    public void setSelectedOrderItem(OrderItem selectedOrderItem) {
        this.selectedOrderItem = selectedOrderItem;
    }
    
}
