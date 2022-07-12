/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.webapp.converters;

import hr.workspace.models.SalesObject;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

/**
 *
 * @author Stjepan
 */
@Named
@FacesConverter(value = "salesObjectConverter", managed = true)
public class SalesObjectConverter extends AbstractEntityConverter<SalesObject>{


    @Override
    Class<SalesObject> getClassType() {
        return SalesObject.class;
    }
}
