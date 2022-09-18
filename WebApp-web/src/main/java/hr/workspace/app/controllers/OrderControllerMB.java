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
import hr.workspace.models.OrderRepresentative;
import hr.workspace.models.Payment;
import hr.workspace.models.Product;
import hr.workspace.models.ProductType;
import hr.workspace.models.Representative;
import hr.workspace.models.UserOrder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Stjepan
 */
@Named(value = "OrderControllerMB")
@ViewScoped
public class OrderControllerMB extends BaseManagedBean {

    @EJB
    ProductCommons productCommons;
    @EJB
    OrderCommons orderCommons;
    @EJB
    DiscountCommons discountCommons;
    private List<UserOrder> orders;
    List<OrderItem> orderItems;
    List<Product> products;
    Map<Product, Integer> productAvailability;

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
            });
        }

        productAvailability = orderCommons.calculateAvailabeQuantityPerProduct(getSecurityContext(), getAllActiveProducts(), orderCommons.getCompletedOrdersForSalesObject(getSecurityContext(), getSalesObject()));
    }

    public void removeOrderItemFromOrder(OrderItem orderItem) {
        UserOrder order = orderController.removeOrderItemFromOrder(getSecurityContext(), getOrder(), orderItem, getUser());
        setOrder(order);
    }

    public void addOrderItemToOrder(Product product) {
        if (product.isBooth()) {

            Optional<OrderItem> orderItem = getOrder().getOrderItems().stream().filter(oi -> ProductType.BOOTH.equals(oi.getProduct().getProductType())).findFirst();
            if (orderItem.isPresent()) {
                addWarningMessage("Booth location is changed", "Location " + orderItem.get().getProduct().getName() + " is changed with " + product.getName());
                removeOrderItemFromOrder(orderItem.get());
            }
        }

        UserOrder order = orderController.addProductToOrder(getSecurityContext(), getOrder(), product, getUser());
        setOrder(order);
        addSuccessMessage(product.getName(), "Sucessfuly added to cart!");
    }

    public void createNewOrder() {
        createNewOrderInternal();
        navigate("order.xhtml?faces-redirect=true");
    }

    public String getNumberOrItemsInOrder() {
        UserOrder order = getOrder();
        if (order != null) {
            int numberOfItemsInOrder = 0;
            if (order.getOrderItems() != null) {
                numberOfItemsInOrder += order.getOrderItems().size();
            }
            if (order.getOrderRepresentatives() != null) {
                numberOfItemsInOrder += order.getOrderRepresentatives().size();
            }
            return Integer.toString(numberOfItemsInOrder);
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

    public List<Product> getAllBoothProducts() {
        return filterProductsByProductType(ProductType.BOOTH);
    }

    public List<Product> getAllFurnitureProducts() {
        return filterProductsByProductType(ProductType.FURNITURE);
    }

    public List<Product> getAllElectronicsProducts() {
        return filterProductsByProductType(ProductType.ELECTRONIC);
    }

    private List<Product> filterProductsByProductType(ProductType productType) {
        return filterProductsByProductType(getAllActiveProducts(), productType);
    }

    private List<Product> filterProductsByProductType(List<Product> products, ProductType productType) {
        return (List<Product>) products.stream().filter(p -> productType.equals(p.getProductType())).collect(Collectors.toList());
    }
    
    public List<Product> getRandomThreeProducts(){
        List<Product> activeProducts = getAllActiveProducts();
        List<Product> products = activeProducts.stream().filter(p -> !ProductType.BOOTH.equals(p.getProductType())).collect(Collectors.toList());
        
        List<UserOrder> orders = getUser().getOrdersForSalesObject(getSalesObject());
        List<Product> productsFromOrders = orders.stream().flatMap(o -> o.getOrderItems().stream().map(oi -> oi.getProduct())).collect(Collectors.toList());
        products.removeAll(productsFromOrders);
        Collections.shuffle(products);
        if(products.size() < 4){
            return products;
        }
        return products.subList(0, 4);
    }

    private Boolean hasUserBooth = null;
        
    public Boolean getHasUserBooth(){
        if(hasUserBooth == null){
            List<UserOrder> orders = getUser().getOrdersForSalesObject(getSalesObject());
            Optional<OrderItem> boothOrderItem = orders.stream().flatMap(o -> o.getOrderItems().stream().filter(oi -> ProductType.BOOTH.equals(oi.getProduct().getProductType()))).findAny();
            hasUserBooth = boothOrderItem.isPresent();
        }
        return hasUserBooth;
    }
    
    public Integer getProductAvailabilityQuantity(Product product) {
        Integer orderedProducts = productAvailability.get(product);
        if (orderedProducts == null) {
            orderedProducts = 0;
        }
        Integer result = product.getQuantity() - orderedProducts;
        return result;
    }

    public Integer getCurrentOrderProductSelectionsQuantity(Product product) {
        Map<Product, Integer> productSelections = new HashMap<>();
        List<OrderItem> orderItems = getOrder().getOrderItems();
        for (OrderItem oi : orderItems) {
            if (productSelections.containsKey(oi.getProduct())) {
                productSelections.put(oi.getProduct(), productSelections.get(oi.getProduct()) + oi.getQuantity());
            } else {
                productSelections.put(oi.getProduct(), oi.getQuantity());
            }
        }
        Integer productSelected = productSelections.get(product);
        return productSelected != null ? productSelected : 0;
    }

    public Integer getCurrentProductAvailability(Product product) {
        Integer availabiltyQuantity = getProductAvailabilityQuantity(product);
        Integer currentOrderProductSelectionsQuantity = getCurrentOrderProductSelectionsQuantity(product);
        return availabiltyQuantity - currentOrderProductSelectionsQuantity;
    }

    public List<OrderRepresentative> getAllOrderRepresentatives() {
        List<Representative> representatives = getUser().getRepresentatives();
        List<OrderRepresentative> orderRepresentatives = new ArrayList<>();
        List<OrderRepresentative> extistingOrderRepresentatives = getOrder().getOrderRepresentatives();
        orderRepresentatives.addAll(extistingOrderRepresentatives);
        List<Representative> existingRepresentatives = extistingOrderRepresentatives.stream().map(o -> o.getRepresentative()).collect(Collectors.toList());
        for (Representative rep : representatives) {
            if (!existingRepresentatives.contains(rep)) {
                OrderRepresentative orderRepresentative = orderController.newOrderRepresentativeFromRepresentative(getSecurityContext(), rep);
                orderRepresentatives.add(orderRepresentative);
            }
        }
        Collections.sort(orderRepresentatives);
        return orderRepresentatives;
    }

    public void addRepresentativeToOrder(OrderRepresentative rep) {
        if (rep != null) {
            UserOrder userOrder = orderController.addOrderRepresentativeToOrder(getSecurityContext(), getOrder(), getUser(), rep);
            if (userOrder != null) {
                setOrder(userOrder);
            }
        }
    }

    public void removeRepresentativeFromOrder(OrderRepresentative rep) {
        if (rep != null) {
            UserOrder userOrder = orderController.removeOrderRepresentativeFromOrder(getSecurityContext(), getOrder(), getUser(), rep);
            if (userOrder != null) {
                setOrder(userOrder);
            }
        }
    }

    public Boolean getCanAddOrderRepresentativeToOrder(OrderRepresentative orderRep) {
        List<Representative> representatived = getOrder().getOrderRepresentatives().stream().map(o -> o.getRepresentative()).collect(Collectors.toList());
        return !representatived.contains(orderRep.getRepresentative());
    }

    public Boolean getCanAddExtrasToRepresentative(OrderRepresentative orderRep) {
        if (orderRep.getUserOrder() != null) {
            return true;
        }
        return false;
    }

    public BigDecimal getPaymentsAmount() {
        List<UserOrder> orders = orderCommons.getAllOrdersForSalesObjectForUser(getSecurityContext(), getUser(), getSalesObject());
        List<Payment> payments = orders.stream().flatMap(o -> o.getPayments().stream()).collect(Collectors.toList());
        BigDecimal result = BigDecimal.ZERO;
        payments.forEach(p -> result.add(p.getAmount()));
        return result;
    }

    public List<UserOrder> getSortedUserOrders() {
        if (orders == null) {
            orders = getUser().getOrdersForSalesObject(getSecurityContext().getSalesObject());
            Collections.sort(orders);
        }
        return orders;
    }

    public List<OrderItem> getUserOrderItems() {

        return orderItems;
    }

    BoothLocation BoothLocation;

    public BoothLocation getBoothInfo() {
        return BoothLocation;
    }

    public List<BoothLocation> getAllBoothLocations() {
        List<Product> booths = getAllBoothProducts();
        List<BoothLocation> result = new ArrayList<>();
        for (Product b : booths) {
            if (getProductAvailabilityQuantity(b) > 0) {
                result.add(new BoothLocation(b));
            }
        }
        return result;
    }

    public List<BoothLocation> getAllSoldBoothLocations() {
        List<Product> booths = getAllBoothProducts();
        List<BoothLocation> result = new ArrayList<>();
        for (Product b : booths) {
            if (getProductAvailabilityQuantity(b) <= 0) {
                result.add(new BoothLocation(b));
            }
        }
        return result;
    }

    public List<BoothLocation> getSelectedBoothLocations() {
        List<BoothLocation> result = new ArrayList<>();
        if (getOrder() != null && !getOrder().getOrderItems().isEmpty()) {
            List<Product> productsBoothsInOrder = filterProductsByProductType(getOrder().getOrderItems().stream().map(oi -> oi.getProduct()).collect(Collectors.toList()), ProductType.BOOTH);
            for (Product b : productsBoothsInOrder) {
                result.add(new BoothLocation(b));
            }
        }
        return result;
    }
    
    public List<OrderItem> getAllBoothOrderItemFromOrders(){
        List<UserOrder> orders = getUser().getOrdersForSalesObject(getSalesObject());
        List<OrderItem> orderItems = orders.stream().flatMap(o -> o.getOrderItems().stream()).collect(Collectors.toList());
        List<OrderItem> boothOrderItems = orderItems.stream().filter(oi -> oi.getProduct().getProductType().equals(ProductType.BOOTH)).collect(Collectors.toList());
        Collections.sort(boothOrderItems,(o1, o2) -> {
            return o1.getId().compareTo(o2.getId());
        });
        return boothOrderItems;
    }

    public void increment() {
        String param1 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("x");

//        Product selectedProduct = productCommons.getObjectById(Product.class, Long.parseLong(param1));
        List<BoothLocation> initProducts = getAllBoothLocations();
        for (BoothLocation p : initProducts) {
            if (p.getId().equals(Long.parseLong(param1))) {
                System.out.println("BoothLocation: " + p.getName());
                BoothLocation = p;
                break;
            }
        }
    }

    public List<BoothLocation> getInitBoothLocations() {
        return getAllBoothLocations();
    }


    public class BoothLocation {

        private Long id;
        private Product product;
        private BoothLocationCoordinates coordinates;
        private String name;
        private BigDecimal price;

        public BoothLocation(Product p) {
            this.product = p;
            this.id = p.getId();
            this.name = p.getName();
            this.price = p.getPrice();
            populateCordinates(p);
        }

        private void populateCordinates(Product p) {
            BoothLocationCoordinates coords = new BoothLocationCoordinates();
            try {

                if (p.getCoordinates() == null) {
                    addErrorMessage("Product " + p.getName() + "[ " + p.getId() + " ]" + " don't have coordingates");
                    return;
                }

                String coordinates1 = p.getCoordinates();

                String[] points = coordinates1.split(";");
                String onePoint = points[0];
                String[] onePointSplitted = onePoint.split(",");
                Double x1 = Double.parseDouble(onePointSplitted[0]);
                Double y1 = Double.parseDouble(onePointSplitted[1]);

                String secondPoint = points[1];
                String[] secondPointSplitted = secondPoint.split(",");
                Double x2 = Double.parseDouble(secondPointSplitted[0]);
                Double y2 = Double.parseDouble(secondPointSplitted[1]);

                coords = new BoothLocationCoordinates(x1, y1, x2, y2);

            } catch (Exception e) {
                e.printStackTrace();
            }
            this.coordinates = coords;
        }

        public Long getId() {
            return id;
        }

        public BoothLocationCoordinates getCoordinates() {
            return coordinates;
        }

        public String getName() {
            return name;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public String getBoothLocationWidth() {
            Double width = getCoordinates().getX2() - getCoordinates().getX1();
            return width.toString();
        }

        public String getBoothLocationHeight() {
            Double height = getCoordinates().getY2() - getCoordinates().getY1();
            return height.toString();
        }
    }

    public class BoothLocationCoordinates {

        Double x1;
        Double y1;
        Double x2;
        Double y2;

        public BoothLocationCoordinates() {
            this(0.0, 0.0, 0.0, 0.0);
        }

        public BoothLocationCoordinates(Double x1, Double y1, Double x2, Double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public Double getX1() {
            return x1;
        }

        public Double getY1() {
            return y1;
        }

        public Double getX2() {
            return x2;
        }

        public Double getY2() {
            return y2;
        }

        public String getCoordsAsString() {
            return "" + x1 + "," + y1 + "," + x2 + "," + y2;
        }
    }

}
