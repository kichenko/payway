/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components;

import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CheckBox;

/**
 * CheckBoxEx
 *
 * @author Sergey Kichenko
 * @created 24.08.2015
 */
public class CheckBoxEx extends CheckBox {

    private static final long serialVersionUID = -7547612420443509507L;

    protected boolean fireValueChange = true;

    public CheckBoxEx() {
        super();
    }

    public CheckBoxEx(String caption) {
        super(caption);
    }

    public CheckBoxEx(String caption, Property<?> dataSource) {
        super(caption, dataSource);
    }

    public CheckBoxEx(String caption, boolean initialState) {
        super(caption, initialState);
    }

    public void setValueEx(Boolean newValue, boolean fireEvent) throws Property.ReadOnlyException, Converter.ConversionException {
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
