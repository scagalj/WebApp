/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.common;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 *
 * @author luka
 */
public class EJBLookup {

    public static <T extends Object> T lookup(Class<? super T> clazz) {
        try {

            Context ic = new InitialContext();

            final Object o = ic.lookup("java:comp/env/ejb/" + clazz.getSimpleName());
            // System.out.println("Result: "+o);
            return (T) o;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
