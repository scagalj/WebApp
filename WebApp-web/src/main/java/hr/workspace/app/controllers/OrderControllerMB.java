/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.app.controllers;

import hr.workspace.controllers.interfaces.DiscountCommons;
import hr.workspace.controllers.interfaces.OrderCommons;
import hr.workspace.controllers.interfaces.OrderController;
import hr.workspace.controllers.interfaces.ProductCommons;
import hr.workspace.models.Discount;
import hr.workspace.models.OrderItem;
import hr.workspace.models.Product;
import hr.workspace.models.ProductType;
import hr.workspace.models.UserOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Stjepan
 */
@Named(value = "OrderControllerMB")
@ViewScoped
public class OrderControllerMB extends BaseManagedBean{
    
    @EJB
    OrderController orderController;
    @EJB
    ProductCommons productCommons;
    @EJB
    OrderCommons orderCommons;
    @EJB
    DiscountCommons discountCommons;
    private List<UserOrder> orders;
    List<OrderItem> orderItems;
    List<Product> products;
    Map<Product,Integer> productAvailability;
    
//    private UserOrder order;
    
    @PostConstruct
    public void init() {
            System.out.println("TEST MAKE ORDER INIT");
        if (getOrder() == null) {
            List<UserOrder> orders = orderCommons.getCurrentlyActiveOrderForUser(getSecurityContext(), getUser(), getSalesObject());
            if (orders != null && !orders.isEmpty()) {
                setOrder(orders.get(0));
            }
        }
        
        if (orderItems == null) {
            orderItems = new ArrayList<>();
            for (UserOrder order : getSortedUserOrders()) {
                orderItems.addAll(order.getOrderItems());
            }
            Collections.sort(orderItems, new Comparator<OrderItem>() {
                @Override
                public int compare(OrderItem o1, OrderItem o2) {
                    return o2.getUserOrder().getId().compareTo(o1.getUserOrder().getId());
                }
            } );
        }
        
        productAvailability = orderCommons.calculateAvailabeQuantityPerProduct(getSecurityContext(), getAllActiveProducts(), orderCommons.getCompletedOrdersForSalesObject(getSecurityContext(), getSalesObject()));
    }
    
    public void removeOrderItemFromOrder(OrderItem orderItem){
        UserOrder order = orderController.removeOrderItemFromOrder(getSecurityContext(), getOrder(), orderItem, getUser());
        setOrder(order);
    }
    
    public void addOrderItemToOrder(Product product){
        UserOrder order = orderController.addProductToOrder(getSecurityContext(), getOrder(), product, getUser());
        setOrder(order);
        addSuccessMessage(product.getName(), "Sucessfuly added to cart!");
    }

    protected void createNewOrderInternal() {
        UserOrder newOrder = orderController.newOrder(getSecurityContext(), getUser(), getSalesObject());
        newOrder = orderController.save(getSecurityContext(), newOrder, getUser());
        setOrder(newOrder);
    }
    
    public void createNewOrder() {
        createNewOrderInternal();
        navigate("order.xhtml?faces-redirect=true");
    }
    
    public String getNumberOrItemsInOrder(){
        UserOrder order = getOrder();
        if(order != null){
            return Integer.toString(order.getOrderItems().size());
        }
        return "0";
    }
    
    public List<Product> getAllActiveProducts() {
        if (products == null) {

            products = productCommons.getAllActive(getSecurityContext());
            products = products.stream().filter(p -> !p.getDisabled()).collect(Collectors.toList());

            List<Discount> discount = discountCommons.getAllActiveDiscount(getSecurityContext());
            products = productCommons.updateProductsWithDiscounts(getSecurityContext(), products, discount);
        }

        return products;
    }
    
    public List<Product> getAllBoothProducts(){
        return filterProductsByProductType(ProductType.BOOTH);
    }
    
    public List<Product> getAllFurnitureProducts(){
        return filterProductsByProductType(ProductType.FURNITURE);
    }
    
    public List<Product> getAllElectronicsProducts(){
        return filterProductsByProductType(ProductType.ELECTRONIC);
    }
    
    private List<Product> filterProductsByProductType(ProductType productType){
        return (List<Product>)getAllActiveProducts().stream().filter(p -> productType.equals(p.getProductType())).collect(Collectors.toList());
    }
    
    public Integer getProductAvailabilityQuantity(Product product){
        Integer orderedProducts = productAvailability.get(product);
        if(orderedProducts == null){
            orderedProducts = 0;
        }
        Integer result = product.getQuantity() - orderedProducts;
        return result;
    }
    
    public Integer getCurrentOrderProductSelectionsQuantity(Product product){
        Map<Product, Integer> productSelections = new HashMap<>();
        List<OrderItem> orderItems = getOrder().getOrderItems();
        for(OrderItem oi : orderItems){
            if(productSelections.containsKey(oi.getProduct())){
                productSelections.put(oi.getProduct(), productSelections.get(oi.getProduct()) + oi.getQuantity());
            }else{
                productSelections.put(oi.getProduct(), oi.getQuantity());
            }
        }
        Integer productSelected = productSelections.get(product);
        return productSelected != null ? productSelected : 0;
    }
    
    public Integer getCurrentProductAvailability(Product product){
        Integer availabiltyQuantity = getProductAvailabilityQuantity(product);
        Integer currentOrderProductSelectionsQuantity = getCurrentOrderProductSelectionsQuantity(product);
        return availabiltyQuantity - currentOrderProductSelectionsQuantity;
    }
    
    
    public List<UserOrder> getSortedUserOrders() {
        if(orders == null){
            orders = getUser().getOrders();
            Collections.sort(orders);
        }
        return orders; 
    }
    
    public List<OrderItem> getUserOrderItems() {
        
        return orderItems;
    }
    
}
