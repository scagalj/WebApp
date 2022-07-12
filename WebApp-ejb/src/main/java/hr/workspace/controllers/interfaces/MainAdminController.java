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
public interface MainAdminController<T extends IEntity> extends Destroyable {

    boolean delete(SecurityContext sc, T editingObject);

    T save(SecurityContext sc, T editingObject);

    T reload(SecurityContext sc, T editingObject);
}
