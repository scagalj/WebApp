/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.app.commons;

/**
 *
 * @author Stjepan
 */
public class Navigation {
    
    private static final String LOGIN = "/login.xhtml?faces-redirect=true";
    private static final String DASHBOARD = "/dashboard.xhtml?faces-redirect=true";
    private static final String INDEX = "/index.xhtml?faces-redirect=true";

    public static String getLOGIN() {
        return LOGIN;
    }

    public static String getDASHBOARD() {
        return DASHBOARD;
    }

    public static String getINDEX() {
        return INDEX;
    }
    
    
    
}
