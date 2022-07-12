/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.webapp.admin.controller;

import hr.workspace.controllers.interfaces.ProductController;
import hr.workspace.models.Product;
import hr.workspace.models.SalesObject;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Stjepan
 */
@Named(value = "ProductControllerMB")
@ViewScoped
public class ProductControllerMB extends BaseManagedBean{
    
    @EJB
    private ProductController controller;
    
    private SalesObject salesObject;
    private Product product;

    
    public void createNewProduct(){
        Product tmpProduct = controller.newProduct(getSecurityContext(), getSalesObject());
        if(tmpProduct != null){
            setProduct(tmpProduct);
            showDialog("newProductDialogWidget");
        }
    }
    
    public void editProduct(Product tmpProduct){
        if(tmpProduct != null){
            setProduct(tmpProduct);
            showDialog("newProductDialogWidget");
        }
    }
    
    public void saveProduct(){
        if(getProduct() != null){
            Product tmpSalesObject = controller.saveProduct(getSecurityContext(), getSalesObject(), getProduct());
            if(tmpSalesObject != null){
                setProduct(null);
            }
        }
    }
    
    public void deleteProduct(Product tmpProduct){
        if(tmpProduct != null){
            Boolean isObjectDeleted = controller.deleteProduct(getSecurityContext(), tmpProduct);
        }
    }
    
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void listenerZaTest(SelectEvent  event){
        Object source = event.getObject();
        if(source != null){
            System.out.println("222222222222222222222222222222222222TEST TEST TEST");
            
        }else{
            System.out.println("TEST TEST TEST");
            
        }
    }
    
    public SalesObject getSalesObject() {
        return salesObject;
    }

    public void setSalesObject(SalesObject salesObject) {
        this.salesObject = salesObject;
    }
    
}
