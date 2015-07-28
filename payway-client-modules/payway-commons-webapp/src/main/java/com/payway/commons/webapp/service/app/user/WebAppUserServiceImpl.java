/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.service.app.user;

import com.payway.commons.webapp.core.CommonAttributes;
import com.vaadin.server.VaadinSession;
import org.springframework.stereotype.Component;

/**
 * WebAppUserServiceImpl
 *
 * Warning in background thread, used vaadin session as user storage!
 *
 * @author Sergey Kichenko
 * @created 01.07.15 00:00
 */
@Component(value = "app.WebAppUserService")
public class WebAppUserServiceImpl implements WebAppUserService {

    protected boolean setSessionValue(String key, Object value) {

        VaadinSession session = VaadinSession.getCurrent();
        if (session == null) {
            return false;
        }

        session.setAttribute(key, value);
        return true;
    }

    protected Object getSessionValue(String key) {

        VaadinSession session = VaadinSession.getCurrent();
        if (session == null) {
            return null;
        }

        return session.getAttribute(key);
    }

    @Override
    public WebAppUser getUser() {
        return (WebAppUser) getSessionValue(CommonAttributes.USER.value());
    }

    @Override
    public boolean setUser(WebAppUser user) {
        return setSessionValue(CommonAttributes.USER.value(), user);
    }
}
