/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers.interfaces;

import hr.workspace.models.SalesObject;
import hr.workspace.security.SecurityContext;

/**
 *
 * @author Stjepan
 */
public interface SalesObjectController {
    
    SalesObject newSalesObject(SecurityContext sc);
    
    SalesObject save(SecurityContext sc, SalesObject so);
    
    Boolean deleteSalesObject(SecurityContext sc, SalesObject so);
}
