/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.webapp.admin.controller;

import hr.workspace.controllers.interfaces.UserCommons;
import hr.workspace.models.ContactUser;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Stjepan
 */
@Named(value = "UserCommonsMB")
@RequestScoped
public class UserCommonsMB extends BaseManagedBean{
    
    @EJB
    private UserCommons commons;
    
    public List<ContactUser> getAllUsers(){
        List<ContactUser> result = commons.getAll(getSecurityContext());
        return result;
    }
    
    public List<ContactUser> getAllActiveUsers(){
        List<ContactUser> result = commons.getAllActive(getSecurityContext());
        return result;
    }
    
    
    
    public List<ContactUser> autoCompleteContactUser(String query){
        List<ContactUser> allActiveSalesObject = getAllActiveUsers();
        List<ContactUser> result = allActiveSalesObject.stream().filter(so -> so.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
        return result;
    }
    
}
