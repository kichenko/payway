/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Базовая сущность
 *
 * @author Сергей Киченко
 * @created 29.04.15 00:00
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEntity<ID extends Serializable> {

    protected ID id;
}
