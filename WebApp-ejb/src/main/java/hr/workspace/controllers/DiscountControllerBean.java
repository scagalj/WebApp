/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.controllers.interfaces.DiscountController;
import hr.workspace.models.ContactUser;
import hr.workspace.models.Discount;
import hr.workspace.models.Product;
import hr.workspace.security.SecurityContext;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 *
 * @author Stjepan
 */
@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class DiscountControllerBean extends MainAdminTransactionControllerBean<Discount> implements DiscountController {

    @Override
    public Discount newDiscount(SecurityContext sc) {
        try {
            Discount result = new Discount();
            return result;
        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return null;
    }

    @Override
    public Discount saveDiscount(SecurityContext sc, Discount discount) {
        try {
            utx.begin();
            Discount result = super.save(sc, discount);

//            Discount discount = new Discount();
//            discount.setAmount(BigDecimal.ONE);
//            discount.setDiscounts(Arrays.asList(result));
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
        return discount;
    }

    @Override
    public Boolean deleteDiscount(SecurityContext sc, Discount discount) {
        try {

            Set<Product> products = new HashSet<>();
            products.addAll(discount.getProducts());
            Set<ContactUser> users = new HashSet<>();
            users.addAll(discount.getContactUsers());
            utx.begin();

            for (ContactUser c : users) {
                discount.getContactUsers().remove(c);
//                discount = removeContactUser(sc, discount, c);
            }
            for (Product p : products) {
                discount.getProducts().remove(p);
//                discount = removeProduct(sc, discount, p);
            }

            discount = merge(discount);
            boolean result = remove(discount);
            utx.commit();
            return result;

        } catch (Exception ex) {
            log(sc, Level.ALL, ex, true);
        }
        return false;
    }

//    private Discount removeContactUser (SecurityContext sc, Discount discount, ContactUser contactUser) {
//        try {
//            discount.getContactUsers().remove(contactUser);
//            discount = merge(discount);
//            return discount;
//        } catch (Exception e) {
//            log(sc, Level.ALL, e, true);
//        }
//        return null;
//    }
//    
//    private Discount removeProduct (SecurityContext sc, Discount discount, Product product) {
//        try {
//            discount.getProducts().remove(product);
//            discount = merge(discount);
//            return discount;
//        } catch (Exception e) {
//            log(sc, Level.ALL, e, true);
//        }
//        return null;
//    }
    @Override
    public Discount addContactUser(SecurityContext sc, Discount discount, ContactUser user) {
        try {

            if (!discount.getContactUsers().contains(user)) {
                utx.begin();

                discount.getContactUsers().add(user);
                discount = merge(discount);
                utx.commit();
            }
            return discount;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }

    @Override
    public Discount removeContactUser(SecurityContext sc, Discount discount, ContactUser user) {
        try {
            if (discount.getContactUsers().contains(user)) {
                utx.begin();
                discount.getContactUsers().remove(user);
                discount = merge(discount);
                utx.commit();
            }
            return discount;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }
    @Override
    public Discount addProduct(SecurityContext sc, Discount discount, Product product) {
        try {

            if (!discount.getProducts().contains(product)) {
                utx.begin();

                discount.getProducts().add(product);
                discount = merge(discount);
                utx.commit();
            }
            return discount;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }

    @Override
    public Discount removeProduct(SecurityContext sc, Discount discount, Product product) {
        try {
            if (discount.getProducts().contains(product)) {
                utx.begin();
                discount.getProducts().remove(product);
                discount = merge(discount);
                utx.commit();
            }
            return discount;
        } catch (Exception e) {
            log(sc, Level.ALL, e, true);
        }
        return null;
    }

}
