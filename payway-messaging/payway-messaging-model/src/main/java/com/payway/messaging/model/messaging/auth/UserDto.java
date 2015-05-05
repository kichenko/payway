/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.model.messaging.auth;

import java.util.ArrayList;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Базовая реализация пользователя.
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"authorities"})
public class UserDto implements User {

    private static final long serialVersionUID = 942608076596562119L;

    private String username;
    private String password;
    private String userToken;

    private Boolean isEnabled = Boolean.FALSE;
    private Collection<Authority> authorities = new ArrayList<>(0);

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUserToken() {
        return userToken;
    }

    @Override
    public Boolean getIsEnabled() {
        return isEnabled;
    }

    @Override
    public Collection<Authority> getAuthorities() {
        return authorities;
    }
}
