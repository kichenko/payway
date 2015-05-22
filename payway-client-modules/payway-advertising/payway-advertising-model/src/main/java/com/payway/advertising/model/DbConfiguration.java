/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.model;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
@EqualsAndHashCode(callSuper = true, exclude = "files")
public class DbConfiguration extends DbAbstractEntity{

    protected String name;
    protected DbUser user;
    protected Set<DbAgentFile> files = new HashSet<>();

    public DbConfiguration(Long id, String name, DbUser user) {
        super(id);
        setName(name);
        setUser(user);
    }

    public DbConfiguration(String name, DbUser user) {
        setName(name);
        setUser(user);
    }
}
