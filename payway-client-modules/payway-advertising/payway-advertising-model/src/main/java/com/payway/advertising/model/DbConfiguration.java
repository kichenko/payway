/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.model;

import java.util.ArrayList;
import java.util.Collection;
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
    protected Collection<DbAgentFile> files = new ArrayList<>();

    public DbConfiguration(Long id, String name, DbUser user) {
        super(id);
        setName(name);
        setUser(user);
    }

    public DbConfiguration(String name, DbUser user) {
        setName(name);
        setUser(user);
    }

    @Override
    public Object clone() {
        return new DbConfiguration(getId(), getName(), getUser() != null ? (DbUser) getUser().clone() : null);
    }
}
