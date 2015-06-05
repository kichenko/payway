/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.service.app.user;

import com.payway.commons.webapp.core.Attributes;
import com.payway.messaging.model.message.auth.UserDto;
import com.vaadin.server.VaadinSession;
import org.springframework.stereotype.Component;

/**
 * UserAppServiceImpl
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
@Component(value = "userAppService")
public class UserAppServiceImpl implements UserAppService {

    @Override
    public UserDto getUser() {

        UserDto user = null;

        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            user = (UserDto) session.getAttribute(Attributes.USER.value());
        }

        return user;
    }

    @Override
    public boolean setUser(UserDto user) {

        boolean isOk = false;

        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(Attributes.USER.value(), user);
            isOk = true;
        }

        return isOk;
    }
}
