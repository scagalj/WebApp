/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers.interfaces;

import hr.workspace.models.SalesObject;
import hr.workspace.security.SecurityContext;
import java.util.List;

/**
 *
 * @author Stjepan
 */
public interface SalesObjectCommons {
    
    List<SalesObject> getAll(SecurityContext sc);
    
    List<SalesObject> getAllActive(SecurityContext sc);

}
