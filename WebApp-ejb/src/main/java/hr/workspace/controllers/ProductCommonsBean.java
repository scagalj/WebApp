/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.controllers.interfaces.ProductCommons;
import hr.workspace.models.Product;
import hr.workspace.models.SalesObject;
import hr.workspace.security.SecurityContext;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Stjepan
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ProductCommonsBean extends AbstractCommonsBean<Product> implements ProductCommons{
    
    @Override
    public List<Product> getAll(SecurityContext sc) {
        try {
            List<Product> result = em.createNamedQuery(Product.getAll).getResultList();
            return result;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    
    
    @Override
    public List<Product> getAllActive(SecurityContext sc) {
        try {
            List<Product> result = em.createNamedQuery(Product.getAllActive).getResultList();
            return result;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    
    @Override
    public List<Product> getAllForSalesObject(SecurityContext sc, SalesObject salesObject) {
        try {
            List<Product> result = em.createNamedQuery(Product.getAllBySalesObject).setParameter("so", salesObject).getResultList();
            return result;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
}
