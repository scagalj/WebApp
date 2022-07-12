/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers;

import hr.workspace.controllers.interfaces.CommonUtils;
import hr.workspace.security.SecurityContext;
import java.io.Serializable;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Stjepan
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class CommonUtilsBean extends SystemControllerBean implements Serializable, CommonUtils {

    @Override
    public void log(SecurityContext sc, Level logLevel, Throwable exception, boolean sendError) {
        super.log(sc, logLevel, exception, sendError);
    }

}
