/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers.interfaces;

import hr.workspace.models.IEntity;
import hr.workspace.security.SecurityContext;


/**
 *
 * @author luka
 */
public interface MainAdminTransactionController<T extends IEntity> extends MainAdminController<T> {

    T cancel(SecurityContext sc, T editingObject);

    T edit(SecurityContext sc, T editingObject);

    boolean isReadOnly(SecurityContext sc);
}
