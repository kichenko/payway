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
 * Agent file
 *
 * @author Sergey Kichenko
 * @created 05.05.15 00:00
 */
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DbAgentFile extends DbAbstractEntity<Long> implements Cloneable {

    protected String name;
    protected DbFileType kind;
    protected DbAgentFileOwner owner;
    protected String expression;
    protected String digest;
    protected Boolean isCountHits;
    protected DbConfiguration configuration;

    public DbAgentFile(Long id, String name, DbFileType kind, DbAgentFileOwner owner, String expression, String digest, Boolean isCountHits, DbConfiguration configuration) {
        super(id);
        setName(name);
        setKind(kind);
        setOwner(owner);
        setExpression(expression);
        setDigest(digest);
        setIsCountHits(isCountHits);
        setConfiguration(configuration);
    }

    @Override
    public DbAgentFile clone() {
        return new DbAgentFile(getId(), getName(), getKind(), getOwner() != null ? (DbAgentFileOwner) getOwner().clone() : null, getExpression(), getDigest(), getIsCountHits(), getConfiguration() != null ? (DbConfiguration) getConfiguration().clone() : null);
    }

}
