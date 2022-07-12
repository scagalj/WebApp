/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.controllers.interfaces.SalesObjectCommons;
import hr.workspace.controllers.interfaces.SalesObjectController;
import hr.workspace.models.SalesObject;
import hr.workspace.security.SecurityContext;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

/**
 *
 * @author Stjepan
 */
@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class SalesObjectControllerBean extends MainAdminTransactionControllerBean<SalesObject> implements SalesObjectController {

    @EJB
    private SalesObjectCommons commons;

    @Override
    public SalesObject newSalesObject(SecurityContext sc) {
        try {
            SalesObject result = new SalesObject();
            return result;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return null;
    }

    @Override
    public SalesObject save(SecurityContext sc, SalesObject so) {
        try {
            utx.begin();
            SalesObject salesObject = super.save(sc, so);
            return salesObject;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return so;
    }

    @Override
    public Boolean deleteSalesObject(SecurityContext sc, SalesObject so) {
        try {
            //TODO: izbrisati sve poveznice na SalesObject
            
            Boolean success = super.delete(sc, so);
            return success;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return false;
    }
}
