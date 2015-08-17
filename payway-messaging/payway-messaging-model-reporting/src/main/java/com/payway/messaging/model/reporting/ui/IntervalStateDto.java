/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

/**
 * IntervalStateDto
 *
 * @author Sergey Kichenko
 * @created 03.08.15 00:00
 */
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
