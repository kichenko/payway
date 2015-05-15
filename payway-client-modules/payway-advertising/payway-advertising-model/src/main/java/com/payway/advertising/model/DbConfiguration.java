/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.payway.advertising.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Agent DbConfiguration
 *
 * @author Sergey Kichenko
 * @created 15.05.15 00:00
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DbConfiguration extends DbAbstractEntity<Long> implements Cloneable {

    protected String name;
    protected DbUser user;

    public DbConfiguration(Long id, String name, DbUser user) {
        super(id);
        setName(name);
        setUser(user);
    }

    @Override
    public Object clone() {
        return new DbConfiguration(getId(), getName(), getUser() != null ? (DbUser) getUser().clone() : null);
    }
}
