/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.app.controllers;

import hr.workspace.models.Attachment;
import hr.workspace.models.ContactUser;
import hr.workspace.models.SalesObject;
import hr.workspace.models.UserOrder;
import hr.workspace.security.SecurityContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Stjepan
 */
public abstract class BaseManagedBean implements Serializable {

    protected void executeScript(String script) {
        PrimeFaces.current().executeScript(script);
    }

    protected void showDialog(String widgetVar) {
        executeScript("PF('" + widgetVar + "').show();");
    }

    protected void hideDialog(String widgetVar) {
        executeScript("PF('" + widgetVar + "').hide();");
    }

    public void copyToClipboard(String text) {
        executeScript("navigator.clipboard.writeText('" + text + "');");
        addSuccessMessage("Text added to clipboard", text);
    }

    public void navigate(String navigation) {
        if (navigation != null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getApplication().getNavigationHandler().handleNavigation(context, null, navigation);
        }
    }

    public SecurityContext getSecurityContext() {
        return getSessionScopeValue("securityContext", SecurityContext.class);
    }

    protected void setSecurityContext(SecurityContext securityContext) {
        setSessionScopeValue("securityContext", securityContext);
    }

    public void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, facesMsg);
    }

    public void addSuccessMessage(String msg, String detail) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, detail);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, facesMsg);
    }

    public void addWarningMessage(String msg, String detail) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, detail);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, facesMsg);
    }

    public void addSuccessMessage(String msg) {
        addSuccessMessage(msg, msg);
    }

    public final <T extends Object> T getELExpression(Class<? super T> beanClass, String expression) {
        FacesContext context = FacesContext.getCurrentInstance();
        return (T) context.getApplication().evaluateExpressionGet(context, expression, beanClass);
    }

    public UIComponent findComponentById(String id) {
        return FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
    }

    public final <T> T getSessionScopeValue(String name, Class<T> classValue) {
        FacesContext context = FacesContext.getCurrentInstance();
        Object result = context.getExternalContext().getSessionMap().get(name);
        if (result != null) {
            return (T) result;
        } else {
            return null;
        }
    }

    public Object invokeELExpressionMethodWithParams(String expression, Object[] params, Class<?>[] expectedParamTypes) {
        if (expectedParamTypes == null) {
            expectedParamTypes = new Class[]{};
        }

        if (params == null) {
            params = new Object[]{};
        }

        FacesContext ctx = FacesContext.getCurrentInstance();
        MethodExpression methodExpression
                = ctx.getApplication().getExpressionFactory().createMethodExpression(ctx.getELContext(), expression, Void.class, expectedParamTypes);
        return methodExpression.invoke(ctx.getELContext(), params);
    }

    protected Object invokeELExpressionMethodWithParams(String expression, Object... params) {
        Object[] localParams = (params == null) ? new Object[]{} : params;
        Class[] types = Arrays.asList(localParams).stream().map(p -> p.getClass()).toArray(Class[]::new);
        return invokeELExpressionMethodWithParams(expression, params, types);
    }

    public Object invokeELExpressionMethod(String expression) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        MethodExpression methodExpression
                = ctx.getApplication().getExpressionFactory().createMethodExpression(ctx.getELContext(), expression, Void.class, new Class[]{});
        return methodExpression.invoke(ctx.getELContext(), null);
    }

    public final <T> T getRequestScopeValue(String name, Class<T> classValue) {
        FacesContext context = FacesContext.getCurrentInstance();
        return (T) context.getExternalContext().getRequestMap().get(name);
    }

    public final void setSessionScopeValue(String name, Object value) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (value != null) {
            context.getExternalContext().getSessionMap().put(name, value);
        } else {
            context.getExternalContext().getSessionMap().remove(name);
        }
    }

    public final void setViewScopeValue(String name, Object value) {
        FacesContext contect = FacesContext.getCurrentInstance();
        if (value != null) {
            contect.getViewRoot().getViewMap().put(name, value);
        } else {
            contect.getViewRoot().getViewMap().remove(name);
        }
    }

    public final <T> T getViewScopeValue(String name, Class<T> classValue) {
        FacesContext context = FacesContext.getCurrentInstance();
        Object result = context.getViewRoot().getViewMap().get(name);
        if (result != null) {
            return (T) result;
        } else {
            return null;
        }
    }

    public final void setRequestScopeValue(String name, Object value) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (value != null) {
            context.getExternalContext().getRequestMap().put(name, value);
        } else {
            context.getExternalContext().getRequestMap().remove(name);
        }
    }

    public StreamedContent generateStreamedContent(Attachment att) {
        if (att == null || att.getData() == null) {
            return null;
        }
        InputStream stream = new ByteArrayInputStream(att.getData());
        DefaultStreamedContent file = DefaultStreamedContent.builder()
                .name(att.getFileName())
                .contentType(att.getContentType())
                .stream(() -> stream)
                .build();
        return file;
    }

    public byte[] generateImageContent(Attachment att) {
        if (att == null || att.getData() == null) {
            return null;
        }
        return att.getData();
    }

    public ContactUser getUser() {
        return getSecurityContext().getLogedUser();
    }

    public void setUser(ContactUser user) {
        getSecurityContext().setLogedUser(user);
    }
    
        public UserOrder getOrder(){
        return getSecurityContext().getOrder();
    }

    public void setOrder(UserOrder order) {
        getSecurityContext().setOrder(order);
    }

    public SalesObject getSalesObject() {
        return getSecurityContext().getSalesObject();
    }

    public void setSalesObject(SalesObject salesObject) {
        getSecurityContext().setSalesObject(salesObject);
    }

}
