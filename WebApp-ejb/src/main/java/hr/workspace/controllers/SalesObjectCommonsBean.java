/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.controllers.interfaces.SalesObjectCommons;
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
public class SalesObjectCommonsBean extends AbstractCommonsBean<SalesObject> implements SalesObjectCommons{
    
    @Override
    public List<SalesObject> getAll(SecurityContext sc) {
        try {
            List<SalesObject> result = em.createNamedQuery(SalesObject.getAll).getResultList();
            return result;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    
    @Override
    public List<SalesObject> getAllActive(SecurityContext sc) {
        try {
            List<SalesObject> result = em.createNamedQuery(SalesObject.getAllActive).getResultList();
            return result;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    
}
