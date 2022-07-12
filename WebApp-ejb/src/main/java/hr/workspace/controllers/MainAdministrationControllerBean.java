/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.controllers.interfaces.MainAdministrationController;
import hr.workspace.models.ACLUser;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author Stjepan
 */
@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class MainAdministrationControllerBean implements MainAdministrationController{
    @PersistenceContext
    protected EntityManager em;
    
    @Resource
    protected UserTransaction utx;
    
    @Override
    public List<ACLUser> getAllUser(){
        List<ACLUser> result = new ArrayList<>();
        try{
            
            result = (List<ACLUser>)em.createQuery("SELECT u FROM ACLUser u", ACLUser.class).getResultList();
//        ACLUser aclUser = new ACLUser();
//        aclUser.setUserName("Test username2");
//        utx.begin();
//        em.persist(aclUser);
//        utx.commit();
        
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("TEST CONTROLLER");
        
        return result;
    }
    
}
