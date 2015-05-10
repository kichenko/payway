/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.model;

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
public abstract class DbAbstractEntity<ID extends Serializable> {

    protected ID id;
}
