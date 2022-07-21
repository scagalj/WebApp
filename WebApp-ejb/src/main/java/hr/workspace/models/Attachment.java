/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.models;

import hr.workspace.common.FileUtils;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 *
 * @author Stjepan
 */
@Entity
public class Attachment implements IEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "attachment_id_seq")
    private Long id;
    private String fileName;
    private String internalName;
    private String fileDescription;
    private Long dataSize;
    private String contentType;
    @ManyToOne
    private ContactUser contactUser;
    @ManyToOne
    private UserOrder userOrder;
    @ManyToOne
    private Product product;
    @Transient
    private byte[] data;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    public Long getDataSize() {
        return dataSize;
    }

    public void setDataSize(Long dataSize) {
        this.dataSize = dataSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public ContactUser getContactUser() {
        return contactUser;
    }

    public void setContactUser(ContactUser contactUser) {
        this.contactUser = contactUser;
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

    public Attachment loadContentToAttachment() {
        if (getData() == null) {
            Attachment loadedAttachment = FileUtils.loadFileFromDisk(this);
            this.setData(loadedAttachment.getData());
        }
        return this;
    }

    public Attachment loadImageContentToAttachment() {
        if (getData() == null) {
            Attachment loadedAttachment = FileUtils.loadImageFromDisk(this);
            this.setData(loadedAttachment.getData());
        }
        return this;
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
        Attachment other = (Attachment) object;
        if (this.getId() == null && other.getId() == null) {
            return super.equals(other);
        }
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
