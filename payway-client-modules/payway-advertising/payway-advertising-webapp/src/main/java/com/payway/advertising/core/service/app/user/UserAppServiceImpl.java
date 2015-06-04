/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.app.user;

import com.payway.advertising.model.DbConfiguration;
import com.payway.advertising.model.DbUser;
import com.payway.commons.webapp.core.Attributes;
import com.vaadin.server.VaadinSession;
import org.springframework.stereotype.Component;

/**
 * UserAppServiceImpl
 *
 * @author Sergey Kichenko
 * @created 10.05.15 00:00
 */
@Component(value = "userAppService")
public class UserAppServiceImpl implements UserAppService {

    @Override
    public DbUser getUser() {

        DbUser user = null;

        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            user = (DbUser) session.getAttribute(Attributes.USER.value());
        }

        return user;
    }

    @Override
    public boolean setUser(DbUser user) {

        boolean isOk = false;

        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(Attributes.USER.value(), user);
            isOk = true;
        }

        return isOk;
    }

    @Override
    public DbConfiguration getConfiguration() {
        DbConfiguration config = null;

        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            config = (DbConfiguration) session.getAttribute(Attributes.CONFIG.value());
        }

        return config;
    }

    @Override
    public boolean setConfiguration(DbConfiguration config) {

        boolean isOk = false;

        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(Attributes.CONFIG.value(), config);
            isOk = true;
        }

        return isOk;
    }
}
