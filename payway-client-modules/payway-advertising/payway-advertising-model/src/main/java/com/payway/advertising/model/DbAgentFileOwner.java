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
 * Agent file owner
 *
 * @author Sergey Kichenko
 * @created 05.05.15 00:00
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DbAgentFileOwner extends DbAbstractEntity {

    protected String name;
    protected String description;

    public DbAgentFileOwner(Long id, String name, String description) {
        super(id);
        setName(name);
        setDescription(description);
    }
}
