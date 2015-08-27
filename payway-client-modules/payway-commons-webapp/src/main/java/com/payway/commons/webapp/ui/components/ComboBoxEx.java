/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import java.util.Collection;

/**
 * ComboBoxEx
 *
 * @author Sergey Kichenko
 * @created 24.08.2015
 */
public class ComboBoxEx extends ComboBox {

    private static final long serialVersionUID = -7031588545377642229L;

    protected boolean fireValueChange = true;

    public ComboBoxEx() {
        super();
    }

    public ComboBoxEx(String caption) {
        super(caption);
    }

    public ComboBoxEx(String caption, Collection<?> options) {
        super(caption, options);
    }

    public ComboBoxEx(String caption, Container dataSource) {
        super(caption, dataSource);
    }

    public void setValueEx(Object newValue, boolean fireEvent) throws Property.ReadOnlyException {
        setFireValueChange(fireEvent);
        setValue(newValue);
        resetFireValueChange();
    }

    public void setFireValueChange(boolean flag) {
        fireValueChange = flag;
    }

    public void resetFireValueChange() {
        fireValueChange = true;
    }

    @Override
    protected void fireValueChange(boolean repaintIsNotNeeded) {

        if (fireValueChange) {
            fireEvent(new AbstractField.ValueChangeEvent(this));
        }

        if (!repaintIsNotNeeded) {
            markAsDirty();
        }
    }
}
