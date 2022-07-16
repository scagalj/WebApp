/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.controllers.interfaces.DiscountCommons;
import hr.workspace.models.Discount;
import hr.workspace.security.SecurityContext;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
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
//            List<Discount> result = em.createNamedQuery(Discount.getAllActive).getResultList();
//            return result;
            return new ArrayList<>();
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    
}
