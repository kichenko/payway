/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.model.messaging.auth;

import java.io.Serializable;
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
public class UserDto implements Serializable {

    private static final long serialVersionUID = 942608076596562119L;

    private String username;
    private String password;
    private String userToken;

    private Boolean isEnabled = Boolean.FALSE;
    private Collection<Authority> authorities = new ArrayList<>(0);

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserToken() {
        return userToken;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public Collection<Authority> getAuthorities() {
        return authorities;
    }
}
