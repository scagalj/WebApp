/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.common.FileUtils;
import hr.workspace.controllers.interfaces.UserController;
import hr.workspace.models.Attachment;
import hr.workspace.models.ContactUser;
import hr.workspace.models.Discount;
import hr.workspace.models.DiscountType;
import hr.workspace.models.Payment;
import hr.workspace.models.Representative;
import hr.workspace.models.UserOrder;
import hr.workspace.security.SecurityContext;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
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
public class UserControllerBean extends MainAdminTransactionControllerBean<ContactUser> implements UserController {

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
            
//            Discount discount = new Discount();
//            discount.setAmount(BigDecimal.ONE);
//            discount.setContactUsers(Arrays.asList(result));
//            discount.setName("Test discount");
//            discount.setType(DiscountType.AMOUNT);
//            discount.setValidFrom(new Date());
//            discount.setValidTo(new Date());
//
//            utx.begin();
//            persist(discount);
//            utx.commit();
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
            
//            TODO IZBRISATI ORDERE I ATTACHMENTE!!!!
//            TODO IZBRISATI ORDERE I ATTACHMENTE!!!!
//            TODO IZBRISATI ORDERE I ATTACHMENTE!!!!
//            TODO IZBRISATI ORDERE I ATTACHMENTE!!!!
//            TODO IZBRISATI ORDERE I ATTACHMENTE!!!!

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
    public ContactUser saveAttachmen(SecurityContext sc, ContactUser user, UploadedFile file) {
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
        att.setContactUser(user);
        Boolean isFileSaved = FileUtils.saveFileToDisk(att);
        if (isFileSaved) {
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
        } else {
            System.out.println("FILE nije spremljen na disk!!!");
        }
        return user;

    }

    @Override
    public Boolean deleteAttachment(SecurityContext sc, ContactUser user, Attachment att) {
        try {
            Boolean isDeleted = FileUtils.deleteFileFromDisk(att);
            if (isDeleted) {
                user.getAttachments().remove(att);
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

    @Override
    public Representative newRepresentative(SecurityContext sc, ContactUser contactUser){
        Representative representative = new Representative();
        representative.setContactUser(contactUser);
        return representative;
    }
    
    @Override
    public ContactUser removeRepresentativeFromContactUser(SecurityContext sc, ContactUser contactUser, Representative representative) {
        try {

            utx.begin();
            representative.setContactUser(null);
            if(contactUser.getRepresentatives().contains(representative)){
                contactUser.getRepresentatives().remove(representative);
            }
            boolean result = super.remove(representative);
            contactUser = merge(contactUser);
            if (result) {
                utx.commit();
                return contactUser;
            } else {
                utx.rollback();
            }
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception ex1) {
                log(sc, Level.ALL, ex1, true);
            }
            log(sc, Level.ALL, ex, true);
        }
        return contactUser;
    }

    @Override
    public ContactUser addContactToContactUser(SecurityContext sc, ContactUser contactUser, ContactUser contact) {
        try {
            System.out.println("TEST ADD representative START");
            utx.begin();
            if(!contactUser.getContacts().contains(contact)){
                contactUser.getContacts().add(contact);
            }
            if(contact.getId() == null){
                persist(contact);
            }else{
                merge(contact);
            }
            contactUser = merge(contactUser);
            utx.commit();
            System.out.println("TEST ADD contact END");
            return contactUser;
        } catch (Exception ex) {
            log(sc, Level.SEVERE, ex, true);
            try {
                utx.rollback();
            } catch (Exception ex1) {
                log(sc, Level.SEVERE, ex1, true);
            }
        }
        return null;
    }
    
    @Override
    public ContactUser addRepresentativeToContactUser(SecurityContext sc, ContactUser contactUser, Representative representative) {
        try {
            System.out.println("TEST ADD representative START");
            utx.begin();
            if(!contactUser.getRepresentatives().contains(representative)){
                contactUser.getRepresentatives().add(representative);
            }
            if(representative.getId() == null){
                persist(representative);
            }else{
                merge(representative);
            }
            contactUser = merge(contactUser);
            utx.commit();
            System.out.println("TEST ADD representative END");
            return contactUser;
        } catch (Exception ex) {
            log(sc, Level.SEVERE, ex, true);
            try {
                utx.rollback();
            } catch (Exception ex1) {
                log(sc, Level.SEVERE, ex1, true);
            }
        }
        return null;
    }
    
    @Override
    public ContactUser newContact(SecurityContext sc, ContactUser contactUser){
        ContactUser contact = new ContactUser();
        contact.setParentContact(contactUser);
        return contact;
    }
    
    @Override
    public ContactUser removeContactFromContactUser(SecurityContext sc, ContactUser contactUser, ContactUser contact) {
        try {

            utx.begin();
            contact.setParentContact(null);
            if(contactUser.getContacts().contains(contact)){
                contactUser.getContacts().remove(contact);
            }
            boolean result = super.remove(contact);
            contactUser = merge(contactUser);
            if (result) {
                utx.commit();
                return contactUser;
            } else {
                utx.rollback();
            }
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception ex1) {
                log(sc, Level.ALL, ex1, true);
            }
            log(sc, Level.ALL, ex, true);
        }
        return contactUser;
    }

}
