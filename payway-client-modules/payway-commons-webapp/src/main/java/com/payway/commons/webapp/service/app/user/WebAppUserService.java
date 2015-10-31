/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.service.app.user;

/**
 * WebAppUserService
 *
 * @author Sergey Kichenko
 * @created 01.07.15 00:00
 */
public interface WebAppUserService {

    WebAppUser getUser();

    boolean setUser(WebAppUser user);

}
