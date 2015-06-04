/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.view.core;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.CustomComponent;
import javax.servlet.http.Cookie;

/**
 * Абстрактный компонент-представление
 *
 * @author Sergey Kichenko
 * @created 30.04.15 00:00
 */
public abstract class AbstractCustomComponentView extends CustomComponent implements CustomComponentInitialize {

    protected Cookie getCookieByName(String name) {
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }

        return null;
    }
}
