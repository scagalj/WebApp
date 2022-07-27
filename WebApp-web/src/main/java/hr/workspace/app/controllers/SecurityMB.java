/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.app.controllers;

import hr.workspace.app.commons.Navigation;
import hr.workspace.controllers.interfaces.SalesObjectCommons;
import hr.workspace.controllers.interfaces.UserCommons;
import hr.workspace.controllers.interfaces.UserController;
import hr.workspace.models.ContactUser;
import hr.workspace.models.SalesObject;
import hr.workspace.security.SecurityContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Stjepan
 */
@Named(value = "SecurityMB")
@SessionScoped
public class SecurityMB extends BaseManagedBean {

    @EJB
    private UserController userController;
    @EJB
    private UserCommons userCommons;
    @EJB
    private SalesObjectCommons salesObjectCommons;
    private String email;
    

    public String login() {
        try {
            String redirect = getRequestScopeValue("redirectUrl", String.class);
//            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            SecurityContext sc = new SecurityContext();
            ContactUser user = userCommons.fetchUserByEMail(sc, getEmail());
            if (user != null) {
                sc.setLogedUser(user);
                List<SalesObject> salesObjects = salesObjectCommons.getAllActive(getSecurityContext());
                sc.setSalesObject(salesObjects != null && !salesObjects.isEmpty() ? salesObjects.get(0) : null);
                setSecurityContext(sc);
                System.out.println("LOGIN IN: " + user.getName());

                if (redirect != null && !redirect.isEmpty()) {
                    String r = handleRedirect(redirect);
//                    System.out.println("handling redirect 1 " + r);
                    return r;
                }

                String r = Navigation.getINDEX();
                return r;
            }
            System.out.println("Login Failed " + getEmail() + " " + new Date().toString());
//            setUsername(null);
//            setPassword(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Flash flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        return Navigation.getLOGIN();
    }

    public String loginByUUID(String uuid) {
        boolean isLogged = loginWithUUID(uuid);
        if (isLogged) {
            System.out.println("LOGIN IN: " + getSecurityContext().getLogedUser().getName());
            return Navigation.getINDEX();
        }
        System.out.println("Login Failed " + getEmail() + " " + new Date().toString());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Flash flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        return Navigation.getLOGIN();
    }

    private Boolean loginWithUUID(String uuid) {
        try {
            SecurityContext sc = new SecurityContext();
            ContactUser user = userCommons.fetchUserByUUID(sc, uuid);
            if (user != null) {
                sc.setLogedUser(user);
                setSecurityContext(sc);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String logout() {
        try {
            System.out.println("LOGOUT user [" + getSecurityContext().getLogedUser().getName() + "] - " + new Date());
        } catch (Exception e) {
//            e.printStackTrace();
        }
        setSecurityContext(null);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return Navigation.getLOGIN();
    }

    public void loginOnLoad() {
        try {
            System.out.println("LOGIN ON LOAD POZVAN");
            FacesContext context = FacesContext.getCurrentInstance();
//        String redirect = context.getExternalContext().getRequestParameterMap().get("redirect");
            String uuid = context.getExternalContext().getRequestParameterMap().get("uuid");
            System.out.println("UUID: " + uuid);
            if (uuid != null) {
                Boolean isLogged = loginWithUUID(uuid);
                if (isLogged) {
                    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
                    String requestPath = request.getContextPath();
                    context.getExternalContext().redirect(requestPath + Navigation.getINDEX());
                }
            }
//        setRequestScopeValue("redirectUrl", redirect);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String handleRedirect(String redirect) {
        StringBuilder redirectAction = new StringBuilder(redirect);
        if (redirect.contains("?")) {
            redirectAction.append("&");
        } else {
            redirectAction.append("?");
        }
        redirectAction.append("faces-redirect=true");
        return redirectAction.toString();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
