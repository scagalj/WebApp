/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.webapp.admin.controller;

import hr.workspace.controllers.interfaces.SalesObjectCommons;
import hr.workspace.models.SalesObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 *
 * @author Stjepan
 */
@Named(value = "SalesObjectCommonsMB")
@RequestScoped
public class SalesObjectCommonsMB extends BaseManagedBean{
 
    @EJB
    private SalesObjectCommons commons;
    
    public List<SalesObject> getAllSalesObject(){
        List<SalesObject> salesObjects = commons.getAll(getSecurityContext());
        return salesObjects;
    }
    
    public List<SalesObject> getAllActiveSalesObject(){
        List<SalesObject> salesObjects = commons.getAllActive(getSecurityContext());
        return salesObjects;
    }
    
    public List<SelectItem> getSelectAllActive() {
        try {
            List<SelectItem> result = new ArrayList<>();
            List<SalesObject> list = getAllActiveSalesObject();

            for (SalesObject temp : list) {
                result.add(new SelectItem(temp, temp.getSalesObjectName()));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    
    public List<SalesObject> autoCompleteSalesObjects(String query){
        List<SalesObject> allActiveSalesObject = getAllActiveSalesObject();
        List<SalesObject> result = allActiveSalesObject.stream().filter(so -> so.getSalesObjectName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
        return result;
    }
    
    
    
}
