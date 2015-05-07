/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.model;

import lombok.AllArgsConstructor;
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
public class DbAgentFile extends DbAbstractEntity<Long> {

    protected String name;
    protected DbFileType kind;
    protected DbAgentFileOwner owner;
    protected String expression;
    protected String digest;
}
