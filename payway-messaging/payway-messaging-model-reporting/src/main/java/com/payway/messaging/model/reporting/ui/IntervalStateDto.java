/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

import lombok.Getter;
import lombok.Setter;

/**
 * IntervalStateDto
 *
 * @author Sergey Kichenko
 * @created 03.08.15 00:00
 */
@Getter
@Setter
public class IntervalStateDto extends ComponentStateDto {

    private static final long serialVersionUID = -8488200464648497334L;

    private String from;
    private String to;

    public IntervalStateDto(String name, String caption, String from, String to) {
        setName(name);
        setCaption(caption);
        setFrom(from);
        setTo(to);
    }
}
