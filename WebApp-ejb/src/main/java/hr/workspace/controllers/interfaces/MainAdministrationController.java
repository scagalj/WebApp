/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers.interfaces;

import hr.workspace.models.ACLUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Stjepan
 */
@Remote
public interface MainAdministrationController {
    
    List<ACLUser> getAllUser();
}
