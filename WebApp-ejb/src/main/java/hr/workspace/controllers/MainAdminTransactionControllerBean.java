/*
 * MainAdminController.java
 * 
 * Created on Oct 25, 2007, 1:58:35 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.controllers.interfaces.MainAdminTransactionController;
import hr.workspace.models.IEntity;
import hr.workspace.security.SecurityContext;
import java.util.logging.Level;
import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

/**
 *
 * @author luka
 */
public abstract class MainAdminTransactionControllerBean<T extends IEntity> extends MainAdminControllerBean<T> implements MainAdminTransactionController<T> {

    @Resource
    protected UserTransaction utx;

    @Override
    public T save(SecurityContext sc, T editingObject) {
        try {
            T result = super.save(sc, editingObject);
            if (result != null) {
                utx.commit();
            } else {
                utx.rollback();
            }
            return result;
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception ex1) {
                log(sc, Level.ALL, ex1, true);
            }
            log(sc, Level.ALL, ex, true);
        }
        return null;
    }

    @Override
    public T edit(SecurityContext sc, T editingObject) {
        try {
            editingObject = reload(sc, editingObject);
            if (!isReadOnly(sc)) {
                utx.rollback();
            }
            utx.begin();
            em.setFlushMode(FlushModeType.COMMIT);
            return editingObject;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }

    @Override
    public T cancel(SecurityContext sc, T editingObject) {
        try {
            utx.rollback();
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;

    }

    @Override
    public boolean delete(SecurityContext sc, T editingObject) {
        try {
            utx.begin();
            boolean result = super.delete(sc, editingObject);
            if (result) {
                utx.commit();
            } else {
                utx.rollback();
            }
            return result;
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception ex1) {
                log(sc, Level.ALL, ex1, true);
            }
            log(sc, Level.ALL, e, true);
        }
        return false;
    }

    @Override
    public boolean isReadOnly(SecurityContext sc) {
        try {
            return utx.getStatus() == Status.STATUS_NO_TRANSACTION;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return true;
    }

    @Override
    public T reload(SecurityContext sc, T editingObject) {
        try {
            utx.begin();
            T result = (T) em.find(editingObject.getClass(), editingObject.getId());
            em.refresh(result);
            utx.commit();
            return result;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
}
