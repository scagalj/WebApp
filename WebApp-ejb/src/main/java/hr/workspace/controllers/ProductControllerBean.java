/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.controllers.interfaces.ProductController;
import hr.workspace.models.Product;
import hr.workspace.models.SalesObject;
import hr.workspace.security.SecurityContext;
import java.util.logging.Level;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 *
 * @author Stjepan
 */
@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class ProductControllerBean extends MainAdminTransactionControllerBean<Product> implements ProductController{

    @Override
    public Product newProduct(SecurityContext sc, SalesObject salesObject) {
        try {
            if(salesObject == null){
                return null;
            }
            Product result = new Product();
            result.setSalesObject(salesObject);
            return result;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    
    @Override
    public Product saveProduct(SecurityContext sc, SalesObject salesObject, Product product) {
        try {
            if(salesObject != null && product != null){
                utx.begin();
                if(product.getId() == null){
//                    if(!salesObject.getProducts().contains(product)){
//                        salesObject.getProducts().add(product);
//                        salesObject = merge(salesObject);
//                    }
                }
                product = super.save(sc, product);
                return product;
            }
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    
    @Override
    public Boolean deleteProduct(SecurityContext sc, Product product) {
        try {
            product.getSalesObject().getProducts().remove(product);
            product.setSalesObject(null);
            boolean removed = super.delete(sc, product);
            return removed;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return false;

    }
    
    @Override
    public Product cancel(SecurityContext sc, Product editingObject) {
        return super.cancel(sc, editingObject); //To change body of generated methods, choose Tools | Templates.
    }

   

   
    
    
    
}
