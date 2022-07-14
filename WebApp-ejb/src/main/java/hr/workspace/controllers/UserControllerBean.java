/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.common.FileUtils;
import hr.workspace.controllers.interfaces.UserController;
import hr.workspace.models.Attachment;
import hr.workspace.models.SalesObject;
import hr.workspace.models.ContactUser;
import hr.workspace.models.UserOrder;
import hr.workspace.security.SecurityContext;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Stjepan
 */
@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class UserControllerBean extends MainAdminTransactionControllerBean<ContactUser> implements UserController{
    
     @Override
    public ContactUser newUser(SecurityContext sc, String email) {
        try {
            ContactUser result = new ContactUser(email);
            return result;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return null;
    }

    @Override
    public ContactUser saveUser(SecurityContext sc, ContactUser so) {
        try {
            utx.begin();
            String uuid = UUID.nameUUIDFromBytes(so.getEmail().getBytes()).toString();
            so.setUuid(uuid);
            so.setUniqueId(uuid);
            ContactUser result = super.save(sc, so);
            return result;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return so;
    }
    
    @Override
    public Boolean deleteUser(SecurityContext sc, ContactUser so) {
        try {
            //TODO: izbrisati sve poveznice na User
            //TODO maknuti sve reference sa UserOrder
            
//            List<UserOrder> orderItems = so.getOrders();
//            for (UserOrder uo : new ArrayList<>(orderItems)) {
//                deleteOrder(sc, uo);
//            }
            
            Boolean success = super.delete(sc, so);
            return success;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return false;
    }
    
    @Override
    public ContactUser saveAttachmen(SecurityContext sc, ContactUser user, UploadedFile file){
        if(file.getSize() <= 0){
            return null;
        }
        Attachment att = new Attachment();
        att.setContentType(file.getContentType());
        att.setFileName(file.getFileName());
        att.setInternalName(file.getFileName() + "_" + UUID.randomUUID().toString());
        att.setDataSize(file.getSize());
        att.setFileDescription("Radnom tekst za sad");
        att.setData(file.getContent());
        att.setContactUser(user);
         Boolean isFileSaved = FileUtils.saveFileToDisk(att);
        if(isFileSaved){
            try {
                utx.begin();
                em.persist(att);
                user.getAttachments().add(att);
                user = merge(user);
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
        }else{
            System.out.println("FILE nije spremljen na disk!!!");
        }
        return user;
        
    }
    
}
