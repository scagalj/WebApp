/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.webapp.admin.controller;

import hr.workspace.controllers.interfaces.ProductController;
import hr.workspace.models.Attachment;
import hr.workspace.models.Product;
import hr.workspace.models.SalesObject;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFiles;

/**
 *
 * @author Stjepan
 */
@Named(value = "ProductControllerMB")
@ViewScoped
public class ProductControllerMB extends BaseManagedBean{
    
    private final static String PRODUCT_DIALOG_NAME = "newProductDialogWidget";
    
    @EJB
    private ProductController controller;
    
    private SalesObject salesObject;
    private Product product;
    private UploadedFiles attachments;

    
    public void createNewProduct(){
        Product tmpProduct = controller.newProduct(getSecurityContext(), getSalesObject());
        if(tmpProduct != null){
            setProduct(tmpProduct);
            showDialog(PRODUCT_DIALOG_NAME);
        }
    }
    
    public void editProduct(Product tmpProduct){
        if(tmpProduct != null){
            setProduct(tmpProduct);
            showDialog(PRODUCT_DIALOG_NAME);
        }
    }
    
    public void saveProduct(){
        if(getProduct() != null){
            Product tmpSalesObject = controller.saveProduct(getSecurityContext(), getSalesObject(), getProduct());
            if(tmpSalesObject != null){
//                setProduct(null);
            }
        }
    }
    
    public void closeProduct(){
        setProduct(null);
        hideDialog(PRODUCT_DIALOG_NAME);
    }
    
    public void deleteProduct(Product tmpProduct){
        if(tmpProduct != null){
            Boolean isObjectDeleted = controller.deleteProduct(getSecurityContext(), tmpProduct);
        }
    }
    
    public void uploadMultiple() {
        if (getAttachments() != null) {
            for (UploadedFile f : getAttachments().getFiles()) {
                controller.saveAttachmen(getSecurityContext(), getProduct(), f);
                System.out.println("FILE: " + f.getFileName());
            }
            setAttachments(null);
        }
    }

    public void deleteAttachment(Attachment att) {
        Boolean deleteAttachment = controller.deleteAttachment(getSecurityContext(), getProduct(), att);
        if (deleteAttachment) {
            System.out.println("Uspijesno izbrisano.");
        } else {
            System.out.println("NIje uspio izbrisati attachment.");
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

    public UploadedFiles getAttachments() {
        return attachments;
    }

    public void setAttachments(UploadedFiles attachments) {
        this.attachments = attachments;
    }
    
    public SalesObject getSalesObject() {
        return salesObject;
    }

    public void setSalesObject(SalesObject salesObject) {
        this.salesObject = salesObject;
    }
    
}
