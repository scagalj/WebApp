/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.webapp.admin.controller;

import hr.workspace.controllers.interfaces.SalesObjectController;
import hr.workspace.models.SalesObject;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Stjepan
 */
@Named(value = "SalesObjectControllerMB")
@ViewScoped
public class SalesObjectControllerMB extends BaseManagedBean{

    @EJB
    private SalesObjectController controller;
    private SalesObject salesObject;
    
    public void createNewSalesObject(){
        SalesObject tmpSalesObject = controller.newSalesObject(getSecurityContext());
        if(tmpSalesObject != null){
            setSalesObject(tmpSalesObject);
                showDialog("newSalesObjectDialogWidget");
        }
    }
    
    public void editSalesObject(SalesObject tmpSalesObject){
        if(tmpSalesObject != null){
            setSalesObject(tmpSalesObject);
            showDialog("newSalesObjectDialogWidget");
        }
    }

    public void saveSalesObject(){
        if(getSalesObject() != null){
            SalesObject tmpSalesObject = controller.save(getSecurityContext(), getSalesObject());
            if(tmpSalesObject != null){
                setSalesObject(null);
            }
        }
    }
    
    public void deleteSalesObject(SalesObject tmpSalesObject){
        if(tmpSalesObject != null){
            Boolean isObjectDeleted = controller.deleteSalesObject(getSecurityContext(), tmpSalesObject);
        }
    }
    
    public SalesObject getSalesObject() {
        return salesObject;
    }

    public void setSalesObject(SalesObject salesObject) {
        this.salesObject = salesObject;
    }
    
}
