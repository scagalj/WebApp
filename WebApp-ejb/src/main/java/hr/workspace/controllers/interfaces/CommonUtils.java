/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.controllers.interfaces;

import hr.workspace.security.SecurityContext;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author Stjepan
 */
public interface CommonUtils {
 
    void destroyEclipseLinkCache();

    <T> T getObjectById(Class<T> clazz, Long id);

//    Object getObjectFromSearchDocument(SecurityContext securityContext, Document document);


    <T> T refresh(T object);

    <T> T merge(T object);



    void log(SecurityContext sc, Level logLevel, Throwable exception, boolean sendError);
    
}
