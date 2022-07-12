/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.managebeans;

import hr.workspace.controllers.interfaces.SalesObjectCommons;
import hr.workspace.controllers.interfaces.SalesObjectController;
import hr.workspace.controllers.interfaces.MainAdministrationController;
import hr.workspace.models.ACLUser;
import hr.workspace.models.SalesObject;
import hr.workspace.security.SecurityContext;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Stjepan
 */
@Named("MainAdministrationControllerMB")
@ViewScoped
public class MainAdministrationControllerMB implements Serializable{
    SecurityContext securityContext = new SecurityContext();
    @EJB
    private MainAdministrationController controller;
    @EJB
    private SalesObjectController salesObjectController;
    @EJB
    private SalesObjectCommons salesObjectCommons;
    private SalesObject salesObject;
    
    public void newSalesObject(){
        System.out.println("NOVI SALES OBJECT");
        setSalesObject(salesObjectController.newSalesObject(getSecurityContext()));
    }
    
    public void saveSalesObject(){
        if(getSalesObject() != null){
            salesObjectController.save(getSecurityContext(), getSalesObject());
            setSalesObject(null);
        }
    }

    public List<SalesObject> getAllSalesObject(){
        return salesObjectCommons.getAll(getSecurityContext());
    }
    
    public String getAllACLUsers(){
        List<ACLUser> allUser = controller.getAllUser();
        for(ACLUser user : allUser){
            System.out.println("ID: " + user.getId() + " Name: " + user.getUserName());
        }
        return "Test users";
    }

    public SalesObject getSalesObject() {
        return salesObject;
    }

    public void setSalesObject(SalesObject salesObject) {
        this.salesObject = salesObject;
    }

    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public void setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    
}
