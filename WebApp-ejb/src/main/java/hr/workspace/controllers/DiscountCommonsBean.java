/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.controllers.interfaces.DiscountCommons;
import hr.workspace.models.ContactUser;
import hr.workspace.models.Discount;
import hr.workspace.models.Product;
import hr.workspace.models.UserOrder;
import hr.workspace.security.SecurityContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

/**
 *
 * @author Stjepan
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class DiscountCommonsBean extends AbstractCommonsBean<Discount> implements DiscountCommons {
    
     @Override
    public List<Discount> getAll(SecurityContext sc) {
        try {
            List<Discount> result = em.createNamedQuery(Discount.getAll).getResultList();
            return result;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    
    @Override
    public List<Discount> getAllActive(SecurityContext sc) {
        try {
            List<Discount> result = em.createNamedQuery(Discount.getAllActive).getResultList();
            return result;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    
    @Override
    public List<Discount> getAllActiveDiscount(SecurityContext sc) {
        try {
            List<Discount> result = em.createNamedQuery(Discount.getAllActiveDiscount).getResultList();
            return result;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    
    @Override
    public Discount getPromoCodeForUser(SecurityContext sc, ContactUser user, UserOrder order, String promoCode) {
        try {
            Query q = em.createQuery("Select d from Discount d Where d.promoCode = true and d.promoCodeValue like :promoCode");
            q.setParameter("promoCode", promoCode);
            Discount discount = (Discount) q.getSingleResult();
            if(discount.isValid(order, user, null, new Date())){
                return discount;
            }
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    
}
