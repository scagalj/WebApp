/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers.interfaces;

import hr.workspace.models.Product;
import hr.workspace.models.SalesObject;
import hr.workspace.security.SecurityContext;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Stjepan
 */
@Local
public interface ProductCommons{
    
    List<Product> getAll(SecurityContext sc);
    
    List<Product> getAllActive(SecurityContext sc);
    
    List<Product> getAllForSalesObject(SecurityContext sc, SalesObject salesObject);
    
    
}
