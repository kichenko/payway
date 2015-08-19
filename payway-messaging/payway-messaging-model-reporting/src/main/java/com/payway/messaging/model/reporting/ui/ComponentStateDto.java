/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

import com.payway.messaging.model.AbstractDto;

/**
 * ComponentStateDto
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
public abstract class ComponentStateDto extends AbstractDto {

    private static final long serialVersionUID = 6845601714770261635L;

    protected String name;

    protected String caption;

    protected boolean enabled;

    protected boolean visible;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
