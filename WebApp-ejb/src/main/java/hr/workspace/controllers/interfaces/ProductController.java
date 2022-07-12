/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers.interfaces;

import hr.workspace.models.Product;
import hr.workspace.models.SalesObject;
import hr.workspace.security.SecurityContext;
import javax.ejb.Local;

/**
 *
 * @author Stjepan
 */
@Local
public interface ProductController {
    
    Product newProduct(SecurityContext sc, SalesObject salesObject);
    
    Product saveProduct(SecurityContext sc, SalesObject salesObject, Product product);
    
    Boolean deleteProduct(SecurityContext sc, Product product);
}
