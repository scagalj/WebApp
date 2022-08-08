/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.webapp.admin.controller;

import hr.workspace.controllers.interfaces.DiscountCommons;
import hr.workspace.models.Discount;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Stjepan
 */
@Named(value = "DiscountCommonsMB")
@RequestScoped
public class DiscountCommonsMB extends BaseManagedBean{
    
    @EJB
    private DiscountCommons commons;
    
    public List<Discount> getAllDiscounts(){
        List<Discount> result = commons.getAll(getSecurityContext());
        return result;
    }
    
    public List<Discount> getAllActiveDiscounts(){
        List<Discount> result = commons.getAllActive(getSecurityContext());
        return result;
    }
    
}
