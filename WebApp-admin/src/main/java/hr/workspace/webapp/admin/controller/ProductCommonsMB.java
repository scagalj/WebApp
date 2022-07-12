/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.webapp.admin.controller;

import hr.workspace.controllers.interfaces.ProductCommons;
import hr.workspace.models.Product;
import hr.workspace.models.SalesObject;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Stjepan
 */
@Named(value = "ProductCommonsMB")
@RequestScoped
public class ProductCommonsMB extends BaseManagedBean{
    
    @EJB
    private ProductCommons commons;
    
    
    public List<Product> getAllProductsForSalesObject(SalesObject salesObject){
        List<Product> products = commons.getAllForSalesObject(getSecurityContext(), salesObject);
        return products;
    }
    
    
}
