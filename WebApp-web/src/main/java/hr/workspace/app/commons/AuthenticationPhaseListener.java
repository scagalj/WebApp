/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.app.commons;

import hr.workspace.security.SecurityContext;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.MethodExpression;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Stjepan
 */
public class AuthenticationPhaseListener implements PhaseListener {

    private static String LOGIN_PAGE = "/login.xhtml";
    
    private static List<String> unAuthPages = Arrays.asList(LOGIN_PAGE);

    /**
     * Creates a new instance of AuthenticationPhaseListener
     */
    public AuthenticationPhaseListener() {
    }

    @Override
    public void afterPhase(PhaseEvent phaseEvent) {
        FacesContext context = phaseEvent.getFacesContext();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        UIViewRoot viewRoot = context.getViewRoot();
        //boolean isStateLess = context.getRenderKit().getResponseStateManager().isStateless(context, viewRoot.getViewId());

        if (viewRoot != null && viewRoot.getViewId() != null && unAuthPages.contains(viewRoot.getViewId())) {
            return;
        }

        boolean isLogged = userLoged(context);

        boolean isAjax = context.getPartialViewContext().isPartialRequest();
//        boolean isPostback = context.isPostback();
//        boolean isPostback2 = context.getRenderKit().getResponseStateManager().isPostback(context);
        String requestPath = request.getContextPath();
//        String requestURI = request.getRequestURI();
//        String requestViewId = requestURI.substring(requestPath.length());

//            System.out.println("========================");
//            System.out.println("Ajax request: " + isAjax);
//            System.out.println("Is logged: " + isLogged);
//            System.out.println("View root: " + (viewRoot == null ? null : viewRoot.getViewId()));
//            System.out.println("Is postback: " + isPostback);
//            System.out.println("Request path: " + requestPath);
//            System.out.println("Request URI: " + requestURI);
//            System.out.println("Request viewId: " + requestViewId);
//            System.out.println("Request queryParams: " + request.getQueryString());
        if (!isLogged) {
//            String uuid = request.getParameter("uuid");
//            if (uuid != null) {
//                
//            } else {

//                String redirectUrl = buildRedirectUrl(request);
                redirectToLogin(context, isAjax, requestPath);
//            }
        }

    }
    
    public void redirectToLogin(FacesContext context, boolean ajax, String requestPath) {
        try {
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            String url = requestPath + Navigation.getLOGIN();
            String uuid = request.getParameter("uuid");
            if(uuid != null){
                url += "&uuid=" + uuid;
            }
            context.getExternalContext().redirect(url);

            if (ajax) {
                context.getPartialViewContext().setRenderAll(true);
            }
            System.out.println("Redirected to login !!!");
        } catch (IOException ex) {
            Logger.getLogger(AuthenticationPhaseListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void beforePhase(PhaseEvent phaseEvent) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    private boolean userLoged(FacesContext context) {
        SecurityContext sc = (SecurityContext) context.getExternalContext().getSessionMap().get("securityContext");

        return sc != null;

    }

    private String buildRedirectUrl(HttpServletRequest request) {
        String requestPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        String uuid = request.getParameter("uudi");

        String requestViewId = requestURI.substring(requestPath.length());
        StringBuilder result = new StringBuilder(requestViewId != null && !isBlankString(requestViewId) && !requestViewId.equals("/") ? requestViewId : "");

        String queryString = request.getQueryString(); ;
        if (queryString != null && !isBlankString(queryString)) {
            result.append("?").append(queryString);
        }
        return result.toString();

    }

    boolean isBlankString(String string) {
        return string == null || string.trim().isEmpty();
    }

}
