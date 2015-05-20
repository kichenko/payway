/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public abstract class DbAbstractEntity<T extends Serializable> implements Cloneable {

    private T id;

}
