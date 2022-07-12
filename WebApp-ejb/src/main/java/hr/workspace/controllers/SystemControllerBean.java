/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.models.IEntity;
import hr.workspace.security.SecurityContext;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import javax.ejb.Remove;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.persistence.jpa.JpaEntityManager;

/**
 *
 * @author Stjepan
 */
public class SystemControllerBean implements Serializable{
    
    @PersistenceContext
    protected EntityManager em;
    
    protected void log(SecurityContext sc, Level logLevel, Throwable exception, boolean sendError) {
        exception.printStackTrace();
    } 
    
    public <T> T getObjectById(SecurityContext sc, Class<T> clazz, Long id) {
        try {
            T result = em.find(clazz, id);
            return result;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }

    public <T> T getObjectById(Class<T> clazz, Long id) {
        return getObjectById(null, clazz, id);
    }
    
     public void destroyEclipseLinkCache() {
        try {
            ((JpaEntityManager) em.getDelegate()).getServerSession().getIdentityMapAccessor().invalidateAll();
        } catch (Exception e) {
            log(null, Level.ALL, e, true);
        }
    }

    public <T> T refresh(T object) {
        object = em.merge(object);
        em.refresh(object);
        return object;
    }
    
    public <T extends IEntity> T refresh(SecurityContext sc, T document) {
        try {
            document = (T) em.find(document.getClass(), document.getId());
            em.refresh(document);
            return document;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return null;
    }
    
    public <T> T merge(SecurityContext sc, T object) {
        try {
            object = merge(object);
            return object;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return null;
    }
    
    public <T> T merge(T object) {
        return em.merge(object);
    }
    
    public <T> T persist(SecurityContext sc, T object) {
        try {
            persist(object);
            return object;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return null;
    }

    public void persist(Object object) {
        em.persist(object);
    }
    
    
    public boolean remove(Object object) {
        object = merge(object);
        em.remove(object);
        return true;
    }
    
    @Remove
    public void destroy() {
    }
    
}
