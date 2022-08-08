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
import java.util.stream.Collectors;
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
    
    
    public List<Product> getAll(){
        List<Product> products = commons.getAll(getSecurityContext());
        return products;
    }
    public List<Product> getAllActive(){
        List<Product> products = commons.getAllActive(getSecurityContext());
        return products;
    }
    
    public List<Product> autoCompleteProduct(String query) {
        List<Product> products = getAll();

        List<Product> result = products.stream().filter(so -> so.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
        return result;
    }
    
}
