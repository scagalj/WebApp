/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.controllers.interfaces.UserController;
import hr.workspace.models.SalesObject;
import hr.workspace.models.ContactUser;
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
public class UserControllerBean extends MainAdminTransactionControllerBean<ContactUser> implements UserController{
    
     @Override
    public ContactUser newUser(SecurityContext sc) {
        try {
            ContactUser result = new ContactUser();
            return result;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return null;
    }

    @Override
    public ContactUser saveUser(SecurityContext sc, ContactUser so) {
        try {
            utx.begin();
            ContactUser result = super.save(sc, so);
            return result;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return so;
    }

    @Override
    public Boolean deleteUser(SecurityContext sc, ContactUser so) {
        try {
            //TODO: izbrisati sve poveznice na User
            //TODO maknuti sve reference sa UserOrder
            
            Boolean success = super.delete(sc, so);
            return success;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return false;
    }
    
    
}
