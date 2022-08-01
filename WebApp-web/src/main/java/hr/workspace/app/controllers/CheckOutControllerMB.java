/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.app.controllers;

import hr.workspace.models.OrderItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Stjepan
 */
@Named(value = "CheckOutControllerMB")
@ViewScoped
public class CheckOutControllerMB extends BaseManagedBean{

    private List<OrderItem> orderItems = new ArrayList<>();

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
    
}
