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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DbAgentFile extends DbAbstractEntity {

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
}
