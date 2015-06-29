/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.converter.video;

import com.payway.media.core.converter.AbstractConverter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * AbstractVideoConverter
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractVideoConverter extends AbstractConverter implements VideoConverter {

    private static final long serialVersionUID = 955333382183451115L;
}
