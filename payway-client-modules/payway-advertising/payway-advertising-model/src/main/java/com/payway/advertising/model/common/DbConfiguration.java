/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.model.common;

import com.payway.advertising.model.DbAbstractEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DbConfiguration
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DbConfiguration extends DbAbstractEntity {

    private DbConfigurationKeyType key;
    private String value;

}
