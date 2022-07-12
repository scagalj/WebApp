/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.webapp.converters;

import hr.workspace.models.ContactUser;
import hr.workspace.models.Product;
import hr.workspace.models.SalesObject;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

/**
 *
 * @author Stjepan
 */
@Named
@FacesConverter(value = "contactUserConverter", managed = true)
public class ContactUserConverter extends AbstractEntityConverter<ContactUser>{


    @Override
    Class<ContactUser> getClassType() {
        return ContactUser.class;
    }
}
