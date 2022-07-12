/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.webapp.converters;

/**
 *
 * @author Stjepan
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import hr.workspace.common.EJBLookup;
import hr.workspace.controllers.interfaces.CommonUtils;
import hr.workspace.models.IEntity;
import javax.ejb.EJB;
import javax.ejb.EJBs;
import javax.faces.component.UIComponent; //for bean scoping
import javax.faces.context.FacesContext; //for bean declaration
import javax.faces.convert.Converter;

/**
 *
 * @author development
 */
@EJBs({
    @EJB(name = "ejb/CommonUtils", beanInterface = hr.workspace.controllers.interfaces.CommonUtils.class)
})
public abstract class AbstractEntityConverter<T extends IEntity> implements Converter<IEntity> {

//    @EJB
//    protected CommonUtils commonUtils;
    
    abstract Class<T> getClassType();

    @Override
    public IEntity getAsObject(FacesContext context, UIComponent component, String value) {
        if (value.isEmpty()) {
            return null;
        }

        CommonUtils commonUtils = EJBLookup.lookup(CommonUtils.class);

        IEntity entity = commonUtils.getObjectById(getClassType(), Long.parseLong(value));
        return entity;

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, IEntity modelValue) {
        if (modelValue == null) {
            return "";
        }

        if (modelValue instanceof IEntity) {
            IEntity e = (IEntity) modelValue;
            return e.getId().toString();
        }
        return "";

    }

}

