/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.model.User;
import com.payway.advertising.ui.view.core.Attributes;
import com.vaadin.server.VaadinSession;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * UserServiceImpl
 *
 * @author Sergey Kichenko
 * @created 10.05.15 00:00
 */
@Component(value = "userService")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserServiceImpl implements UserService {

    @Override
    public User getUser() {

        User user = null;

        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            user = (User) session.getAttribute(Attributes.USER.value());
        }

        return user;
    }

    @Override
    public boolean setUser(User user) {

        boolean isOk = false;

        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(Attributes.USER.value(), user);
            isOk = true;
        }

        return isOk;
    }
}
