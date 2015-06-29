/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.converter.audio;

import com.payway.media.core.converter.AbstractConverter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * AbstractAudioConverter
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractAudioConverter extends AbstractConverter implements AudioConverter {

    private static final long serialVersionUID = 7814502960049995778L;
}
