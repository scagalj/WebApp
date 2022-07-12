/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.webapp.admin.util;

import hr.workspace.models.ProductType;
import hr.workspace.webapp.admin.controller.BaseManagedBean;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 *
 * @author Stjepan
 */
@Named(value = "SelectMenuItemsMB")
@RequestScoped
public class SelectMenuItemsMB extends BaseManagedBean{
    
    
    public List<SelectItem> getSelectAllProductTypes() {
        try {
            List<SelectItem> result = new ArrayList<>();
            for (ProductType temp : ProductType.values()) {
                result.add(new SelectItem(temp, temp.name()));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    
}
