/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.webapp.admin.controller;

import hr.workspace.controllers.interfaces.OrderCommons;
import hr.workspace.models.UserOrder;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.criteria.Order;

/**
 *
 * @author Stjepan
 */
@Named(value = "OrderCommonsMB")
@RequestScoped
public class OrderCommonsMB extends BaseManagedBean{
 
    @EJB
    private OrderCommons commons;
    
    public List<UserOrder> getAllOrder(){
        List<UserOrder> orders = commons.getAll(getSecurityContext());
        return orders;
    }
    
    public List<UserOrder> getAllActiveOrder(){
        List<UserOrder> orders = commons.getAllActive(getSecurityContext());
        return orders;
    }
    
    
}
