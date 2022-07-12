/*
 * MainAdminController.java
 * 
 * Created on Oct 25, 2007, 1:58:35 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.controllers.interfaces.MainAdminController;
import hr.workspace.models.IEntity;
import hr.workspace.security.SecurityContext;
import java.io.Serializable;
import java.util.logging.Level;

/**
 *
 * @author luka
 */
public abstract class MainAdminControllerBean<T extends IEntity> extends SystemControllerBean implements Serializable, MainAdminController<T> {

    @Override
    public T save(SecurityContext sc, T editingObject) {
        try {
            if (isPersisted(sc, editingObject)) {
                editingObject = merge(editingObject);
            } else {
                persist(editingObject);
            }
            return editingObject;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }

    @Override
    public boolean delete(SecurityContext sc, T editingObject) {
        try {
            return remove(editingObject);
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return false;
    }

    protected boolean isPersisted(SecurityContext sc, T editingObject) {
        try {
            return editingObject.getId() != null;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return false;
    }

    @Override
    public T reload(SecurityContext sc, T editingObject) {
        try {
            T result = (T) em.find(editingObject.getClass(), editingObject.getId());
            em.refresh(result);
            return result;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
}
