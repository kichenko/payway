/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DbUser
 *
 * @author Sergey Kichenko
 * @created 10.05.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DbUser extends DbAbstractEntity {

    protected String login;
    protected String password;
    protected String token;

    public DbUser(Long id, String login, String password, String token) {
        super(id);
        setLogin(login);
        setPassword(password);
        setToken(token);
    }

    @Override
    public Object clone() {
        return new DbUser(getId(), getLogin(), getPassword(), getToken());
    }
}
