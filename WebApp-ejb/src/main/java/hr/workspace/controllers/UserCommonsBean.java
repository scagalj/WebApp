/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.controllers.interfaces.UserCommons;
import hr.workspace.models.SalesObject;
import hr.workspace.models.ContactUser;
import hr.workspace.security.SecurityContext;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

/**
 *
 * @author Stjepan
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class UserCommonsBean extends AbstractCommonsBean<ContactUser> implements UserCommons {
    
     @Override
    public List<ContactUser> getAll(SecurityContext sc) {
        try {
            List<ContactUser> result = em.createNamedQuery(ContactUser.getAll).getResultList();
            return result;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    
    @Override
    public List<ContactUser> getAllActive(SecurityContext sc) {
        try {
            List<ContactUser> result = em.createNamedQuery(ContactUser.getAllActive).getResultList();
            return result;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    
    @Override
    public ContactUser fetchUserByUUID(SecurityContext sc, String uniqueId){
        System.out.println("TEST3");
         Query findUserByUUID = em.createQuery("Select u from ContactUser u where u.uniqueId like :uniqueId")
                 .setParameter("uniqueId", uniqueId);
         List<ContactUser> resultList = findUserByUUID.getResultList();
         if(resultList != null && resultList.size() > 1){
             System.out.println("IMAMO VISE KONTAKTA S ISTIM UUID-om");
         }
         if(resultList == null || resultList.isEmpty()){
             return null;
         }
         return resultList.get(0);
    }
    
    @Override
    public ContactUser fetchUserByEMail(SecurityContext sc, String email){
        System.out.println("TEST3");
         Query findUserByUUID = em.createQuery("Select u from ContactUser u where u.email like :email")
                 .setParameter("email", email);
         List<ContactUser> resultList = findUserByUUID.getResultList();
         if(resultList != null && resultList.size() > 1){
             System.out.println("IMAMO VISE KONTAKTA S ISTIM email-om");
         }
         if(resultList == null || resultList.isEmpty()){
             return null;
         }
         return resultList.get(0);
    }
    
    
    @Override
    public void saveFileToResource(){
        try{
        String value = "Hello";
                String path = this.getClass().getClassLoader().getResource("/").getPath();
            FileOutputStream fos = new FileOutputStream("/opt/attachments/aaaa.txt");
            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
            outStream.writeUTF(value);
            outStream.close();

        
        }catch(Exception ex){
            log(null, Level.SEVERE, ex, true);
        }
        
    }
    
    
}
