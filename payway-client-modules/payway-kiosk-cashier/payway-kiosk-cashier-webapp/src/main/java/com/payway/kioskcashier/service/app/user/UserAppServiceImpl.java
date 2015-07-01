/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.service.app.user;

import com.payway.commons.webapp.core.CommonAttributes;
import com.payway.messaging.model.user.UserDto;
import com.vaadin.server.VaadinSession;
import org.springframework.stereotype.Component;

/**
 * UserAppServiceImpl
 *
 * Warning used in background thread!
 *
 * @author Sergey Kichenko
 * @created 01.07.15 00:00
 */
@Component
public class UserAppServiceImpl implements UserAppService {

    @Override
    public UserDto getUser() {

        VaadinSession session = VaadinSession.getCurrent();
        if (session == null) {
            return null;
        }

        return (UserDto) session.getAttribute(CommonAttributes.USER.value());
    }

    @Override
    public boolean setUser(UserDto user) {

        VaadinSession session = VaadinSession.getCurrent();
        if (session == null) {
            return false;
        }

        session.setAttribute(CommonAttributes.USER.value(), user);
        return true;
    }
}
