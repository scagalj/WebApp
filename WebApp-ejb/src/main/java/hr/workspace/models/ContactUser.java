/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.models;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import hr.workspace.common.FileUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author Stjepan
 */
@Entity
@NamedQueries({
    @NamedQuery(name = ContactUser.getAll, query = "select u from ContactUser u where u.parentContact is null order by u.firstName"),
    @NamedQuery(name = ContactUser.getAllActive, query = "select u from ContactUser u where u.disabled=false and u.parentContact is null order by u.firstName")
})
public class ContactUser implements IEntity, Serializable {

    public final static String getAll = "hr.workspace.models.ContactUser.getAll";
    public final static String getAllActive = "hr.workspace.models.ContactUser.getAllActive";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "contactuser_id_seq")
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    
    private String uuid;
    private String uniqueId;
    
    @OneToMany(mappedBy = "parentContact", cascade = {CascadeType.REMOVE})
    private List<ContactUser> contacts;
    @ManyToOne()
    private ContactUser parentContact;

    private Boolean disabled;

    @OneToMany(mappedBy = "contactUser", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private List<UserOrder> orders;
    
    @OneToMany(mappedBy = "contactUser", fetch = FetchType.EAGER)
    private List<Attachment> attachments;
    
    @OneToMany(mappedBy = "contactUser", fetch = FetchType.EAGER)
    private List<Representative> representatives;

    public ContactUser() {
        this.firstName = "";
        this.lastName = "";
        this.disabled = false;
        this.orders = new ArrayList<>();
        this.attachments = new ArrayList<>();
        this.representatives = new VirtualFlow.ArrayLinkedList<>();
    }
    
    public ContactUser(String email) {
        super();
        this.email = email;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    public List<UserOrder> getOrders() {
        return orders;
    }
    public List<UserOrder> getOrdersForSalesObject(SalesObject salesObject) {
        List<UserOrder> ordersForSalesObject = getOrders().stream().filter(o -> o.getSalesObject().getId().equals(salesObject.getId())).collect(Collectors.toList());
        return ordersForSalesObject;
    }

    public void setOrders(List<UserOrder> orders) {
        this.orders = orders;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }
    
    public List<Attachment> getFinalAttachment(){
        List<Attachment> result = new ArrayList<>();
        for(Attachment att : getAttachments()){
            Attachment tmpAtt = FileUtils.loadFileFromDisk(att);
            if(tmpAtt != null){
                result.add(tmpAtt);
            }
        }
        return result;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Representative> getRepresentatives() {
        return representatives;
    }

    public void setRepresentatives(List<Representative> representatives) {
        this.representatives = representatives;
    }

    public List<ContactUser> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactUser> contacts) {
        this.contacts = contacts;
    }

    public ContactUser getParentContact() {
        return parentContact;
    }

    public void setParentContact(ContactUser parentContact) {
        this.parentContact = parentContact;
    }
    
    public String getName(){
        String name = "";
        if(getFirstName() != null){
            name += getFirstName() + " ";
        }
        if(getLastName() != null){
            name += getLastName() + " ";
        }
        
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        ContactUser other = (ContactUser) object;
        if (this.getId() == null && other.getId() == null) {
            return super.equals(other);
        }
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public List<UserOrder> getUserOrdersByStatus(UserOrderStatus status){
        return getOrders().stream().filter(o -> status.equals(o.getUserOrderStatus())).collect(Collectors.toList());
    }
    
    public BigDecimal getBalance(){
        BigDecimal result = BigDecimal.ZERO;
        List<UserOrder> orders = getUserOrdersByStatus(UserOrderStatus.COMPLETED);
        orders.addAll(getUserOrdersByStatus(UserOrderStatus.AUTHORIZED));
        for(UserOrder tmpOrder : orders){
            for(Payment orderPayment : tmpOrder.getPayments()){
                result = result.add(orderPayment.getAmount());
            }
            result = result.subtract(tmpOrder.getFinalPrice());
        }
        return result;
    }
    
//    public BigDecimal getOrdersAmount(){
//        BigDecimal
//    }
//    
}
