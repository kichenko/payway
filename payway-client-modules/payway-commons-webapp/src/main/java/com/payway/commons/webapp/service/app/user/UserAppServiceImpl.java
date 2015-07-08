/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.service.app.user;

import com.payway.commons.webapp.core.CommonAttributes;
import com.payway.messaging.model.user.UserDto;
import com.vaadin.server.VaadinSession;
import org.springframework.stereotype.Component;

/**
 * UserAppServiceImpl
 *
 * Warning in background thread, used vaadin session as user storage!
 *
 * @author Sergey Kichenko
 * @created 01.07.15 00:00
 */
@Component(value = "webApps.UserAppService")
public class UserAppServiceImpl implements UserAppService {

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
    public UserDto getUser() {
        return (UserDto) getSessionValue(CommonAttributes.USER.value());
    }

    @Override
    public boolean setUser(UserDto user) {
        return setSessionValue(CommonAttributes.USER.value(), user);
    }

    @Override
    public boolean setSessionId(String sessionId) {
        return setSessionValue(CommonAttributes.WEB_APP_SESSION_ID.value(), sessionId);
    }

    @Override
    public String getSessionId() {
        return (String) getSessionValue(CommonAttributes.WEB_APP_SESSION_ID.value());
    }
}
