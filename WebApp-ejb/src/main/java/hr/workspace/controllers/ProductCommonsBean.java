/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.controllers.interfaces.ProductCommons;
import hr.workspace.models.Discount;
import hr.workspace.models.OrderItemDiscount;
import hr.workspace.models.Product;
import hr.workspace.security.SecurityContext;
import java.util.ArrayList;
import java.util.Date;
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
    public List<Product> updateProductsWithDiscounts(SecurityContext sc, List<Product> products, List<Discount> discounts) {
        for (Product p : products) {
            p.setTmpDiscounts(new ArrayList<>());
            for (Discount d : discounts) {
                if (d.isValid(sc.getOrder(), sc.getLogedUser(), p, new Date())) {
                    OrderItemDiscount tmpDisc = new OrderItemDiscount();
                    tmpDisc.setAmount(d.getAmount());
                    tmpDisc.setDiscount(d);
                    tmpDisc.setName(d.getName());
                    tmpDisc.setType(d.getType());
                    if(!p.getTmpDiscounts().contains(tmpDisc)){
                        p.getTmpDiscounts().add(tmpDisc);
                    }
                }
            }
        }
        return products;
    }
    
}
