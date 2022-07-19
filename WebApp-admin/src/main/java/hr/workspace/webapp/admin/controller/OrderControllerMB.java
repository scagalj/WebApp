/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.webapp.admin.controller;

import hr.workspace.controllers.interfaces.OrderController;
import hr.workspace.models.Attachment;
import hr.workspace.models.OrderItem;
import hr.workspace.models.Payment;
import hr.workspace.models.Product;
import hr.workspace.models.UserOrder;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.eclipse.persistence.jpa.jpql.parser.OrderByItem;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFiles;

/**
 *
 * @author Stjepan
 */
@Named(value = "OrderControllerMB")
@ViewScoped
public class OrderControllerMB extends BaseManagedBean {

    private final static String ORDER_DIALOG_NAME = "newOrderDialogWidget";
    private final static String PAYMENT_DIALOG_NAME = "newPaymentDialogWidget";

    @EJB
    private OrderController controller;
    private UserOrder userOrder;
    private Product product;
    private UploadedFiles attachments;
    private Payment payment;

    public void createNewOrder() {
        UserOrder tmpOrder = controller.newOrder(getSecurityContext());
        setProduct(null);
        if (tmpOrder != null) {
            setOrder(tmpOrder);
            showDialog(ORDER_DIALOG_NAME);
        }
    }

    public void editOrder(UserOrder tmpOrder) {
        if (tmpOrder != null) {
            setProduct(null);
            setOrder(tmpOrder);
            showDialog(ORDER_DIALOG_NAME);
        }
    }

    public void saveOrder() {
        if (getOrder() != null) {
            UserOrder tmpOrder = controller.save(getSecurityContext(), getOrder());
            if (tmpOrder != null) {
                setProduct(null);
            }
        }
    }

    public void closeOrder() {
        hideDialog(ORDER_DIALOG_NAME);
        setProduct(null);
        setOrder(null);

    }

    public void deleteOrder(UserOrder tmpOrder) {
        if (tmpOrder != null) {
            Boolean isObjectDeleted = controller.deleteOrder(getSecurityContext(), tmpOrder);
        }
    }

    public void newPayment() {
        Payment newPayment = controller.newPayment(getSecurityContext(), getOrder());
        setPayment(newPayment);
        showDialog(PAYMENT_DIALOG_NAME);
    }

    public void savePayment() {
        if (getPayment() != null) {
            UserOrder order = controller.addPaymentToOrder(getSecurityContext(), getOrder(), getPayment());
            setOrder(order);
        }
    }

    public void removePayment(Payment payment) {
        if (payment != null) {
            UserOrder order = controller.removePaymentFromOrder(getSecurityContext(), getOrder(), payment);
            setOrder(order);
        }
    }

    public void editPayment(Payment payment) {
        if (payment != null) {
            setPayment(payment);
            showDialog(PAYMENT_DIALOG_NAME);
        }
    }

    public void closePayment() {
        setPayment(null);
        hideDialog(PAYMENT_DIALOG_NAME);
    }

    public UserOrder getOrder() {
        return userOrder;
    }

    public void setOrder(UserOrder salesObject) {
        this.userOrder = salesObject;
    }

    public UserOrder getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(UserOrder userOrder) {
        this.userOrder = userOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public UploadedFiles getAttachments() {
        return attachments;
    }

    public void setAttachments(UploadedFiles attachments) {
        this.attachments = attachments;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Boolean getCanEditSalesObject(UserOrder order) {
        if (order == null) {
            return false;
        }

        if (order.getId() != null) {
            return false;
        }
        return true;
    }

    public void onItemSelect(SelectEvent<Product> event) {
        if (event.getObject() != null) {
            Product product = event.getObject();
            System.out.println("ITEM SELECTED: " + product.getName());
            addOrderItemToOrder(getOrder(), product);
            setProduct(null);
        }
    }

    public void addOrderItemToOrder(UserOrder order, Product product) {
        setUserOrder(controller.addProductToOrder(getSecurityContext(), order, product));
    }

    public void removeOrderItemToOrder(UserOrder order, OrderItem orderItem) {
        setUserOrder(controller.removeOrderItemFromOrder(getSecurityContext(), order, orderItem));
    }

    public List<Product> autoCompleteProduct(String query) {
        ProductCommonsMB productCommonsMB = getELExpression(ProductCommonsMB.class, "#{ProductCommonsMB}");
        List<Product> products = productCommonsMB.getAllProductsForSalesObject(getOrder().getSalesObject());

        List<Product> result = products.stream().filter(so -> so.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
        return result;
    }

    public void uploadMultiple() {
        if (getAttachments() != null) {
            for (UploadedFile f : getAttachments().getFiles()) {
                controller.saveAttachmen(getSecurityContext(), getOrder(), f);
                System.out.println("FILE: " + f.getFileName());
            }
            setAttachments(null);
        }
    }

    public void deleteAttachment(Attachment att) {
        Boolean deleteAttachment = controller.deleteAttachment(getSecurityContext(), getOrder(), att);
        if (deleteAttachment) {
            System.out.println("Uspijesno izbrisano.");
        } else {
            System.out.println("NIje uspio izbrisati attachment.");
        }
    }

}
