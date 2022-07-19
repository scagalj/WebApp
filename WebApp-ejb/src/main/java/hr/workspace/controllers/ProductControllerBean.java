/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.common.FileUtils;
import hr.workspace.controllers.interfaces.ProductController;
import hr.workspace.models.Attachment;
import hr.workspace.models.Product;
import hr.workspace.models.SalesObject;
import hr.workspace.models.UserOrder;
import hr.workspace.security.SecurityContext;
import java.util.UUID;
import java.util.logging.Level;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Stjepan
 */
@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class ProductControllerBean extends MainAdminTransactionControllerBean<Product> implements ProductController{

    @Override
    public Product newProduct(SecurityContext sc, SalesObject salesObject) {
        try {
            if(salesObject == null){
                return null;
            }
            Product result = new Product();
            result.setSalesObject(salesObject);
            return result;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    
    @Override
    public Product saveProduct(SecurityContext sc, SalesObject salesObject, Product product) {
        try {
            if(salesObject != null && product != null){
                utx.begin();
                if(product.getId() == null){
//                    if(!salesObject.getProducts().contains(product)){
//                        salesObject.getProducts().add(product);
//                        salesObject = merge(salesObject);
//                    }
                }
                product = super.save(sc, product);
                return product;
            }
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    
    @Override
    public Boolean deleteProduct(SecurityContext sc, Product product) {
        try {
            product.getSalesObject().getProducts().remove(product);
            product.setSalesObject(null);
            boolean removed = super.delete(sc, product);
            return removed;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return false;

    }
    
    @Override
    public Product cancel(SecurityContext sc, Product editingObject) {
        return super.cancel(sc, editingObject); //To change body of generated methods, choose Tools | Templates.
    }

   @Override
    public Product saveAttachmen(SecurityContext sc, Product product, UploadedFile file) {
        if (file.getSize() <= 0) {
            return null;
        }
        Attachment att = new Attachment();
        att.setContentType(file.getContentType());
        att.setFileName(file.getFileName());
        att.setInternalName(file.getFileName() + "_" + UUID.randomUUID().toString());
        att.setDataSize(file.getSize());
        att.setFileDescription("Radnom tekst za sad");
        att.setData(file.getContent());
        att.setProduct(product);
        Boolean isFileSaved = FileUtils.saveImageToDisk(att);
        if (isFileSaved) {
            try {
                utx.begin();
                em.persist(att);
                product.getAttachments().add(att);
                product = merge(product);
                utx.commit();
                System.out.println("FILE uspjesno spremljen na disk!!!");
            } catch (Exception ex) {
                try {
                    utx.rollback();
                } catch (Exception ex1) {
                    log(sc, Level.SEVERE, ex1, true);
                }
                log(sc, Level.SEVERE, ex, true);
            }
        } else {
            System.out.println("FILE nije spremljen na disk!!!");
        }
        return product;

    }

    @Override
    public Boolean deleteAttachment(SecurityContext sc, Product product, Attachment att) {
        try {
            Boolean isDeleted = FileUtils.deleteImageFromDisk(att);
            if (isDeleted) {
                product.getAttachments().remove(att);
                utx.begin();
                if (!em.contains(att)) {
                    att = em.merge(att);
                }
                em.remove(att);
                utx.commit();
                return true;
            }
        } catch (Exception e) {
            log(sc, Level.SEVERE, e, true);
        }
        return false;
    }

   
    
    
    
}
