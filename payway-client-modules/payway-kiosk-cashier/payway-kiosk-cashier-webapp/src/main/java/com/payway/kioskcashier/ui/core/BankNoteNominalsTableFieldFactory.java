/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.core;

import com.payway.commons.webapp.core.utils.NumberFormatConverterUtils;
import com.payway.commons.webapp.core.utils.NumberUtils;
import com.payway.vaadin.addons.ui.textfield.digit.DigitTextField;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.MethodProperty;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;
import java.util.Locale;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * BankNoteNominalsTableFieldFactory
 *
 * @author Sergey Kichenko
 * @created 23.07.15 00:00
 */
@NoArgsConstructor
@AllArgsConstructor
public class BankNoteNominalsTableFieldFactory implements TableFieldFactory {

    private static final long serialVersionUID = 1333748753542828713L;

    public interface FieldRefreshCallbackListener {

        void refresh();
    }

    private String currencyIso;
    private Map<Long, TextField> mapEditors;
    private FieldRefreshCallbackListener callback;

    @Override
    public Field<?> createField(final Container container, Object itemId, Object propertyId, Component uiContext) {

        if ("label".equals(propertyId)) {

            TextField field = new TextField((String) propertyId);
            field.setSizeFull();
            field.setData(itemId);
            field.setReadOnly(true);
            field.setStyleName("borderless app-common-style-text-center");

            return field;

        } else if ("amount".equals(propertyId)) {

            TextField field = new TextField((String) propertyId);
            field.setSizeFull();
            field.setData(itemId);
            field.setReadOnly(true);
            field.setStyleName("borderless app-common-style-text-right");

            field.setConverter(new Converter<String, Double>() {
                private static final long serialVersionUID = -810375086450402998L;

                @Override
                public Double convertToModel(String value, Class<? extends Double> targetType, Locale locale) throws Converter.ConversionException {
                    return null;
                }

                @Override
                public String convertToPresentation(Double value, Class<? extends String> targetType, Locale locale) throws Converter.ConversionException {
                    return String.format("%s %s", NumberUtils.isInteger(value) ? NumberFormatConverterUtils.format(value, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS) : NumberFormatConverterUtils.format(value, NumberFormatConverterUtils.DEFAULT_PATTERN_WITH_DECIMALS), currencyIso);
                }

                @Override
                public Class<Double> getModelType() {
                    return Double.class;
                }

                @Override
                public Class<String> getPresentationType() {
                    return String.class;
                }
            });

            return field;

        } else if ("quantity".equals(propertyId)) {

            final TextField field = new DigitTextField();
            field.setSizeFull();
            field.setMaxLength(4);
            field.setData(itemId);
            field.setImmediate(true);
            field.setInvalidAllowed(false);
            field.setNullRepresentation("0");
            field.setStyleName("app-common-style-text-right");

            mapEditors.put((Long) itemId, field);

            field.setConverter(new Converter<String, Integer>() {
                private static final long serialVersionUID = 4048654578990565270L;
                private final StringToIntegerConverter converter = new StringToIntegerConverter();

                @Override
                public Integer convertToModel(String value, Class<? extends Integer> targetType, Locale locale) throws Converter.ConversionException {

                    Integer result = 0;
                    try {
                        result = converter.convertToModel(value, targetType, locale);
                    } catch (Exception ex) {
                        //NOP
                    }

                    return result == null ? 0 : result;
                }

                @Override
                public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale) throws Converter.ConversionException {
                    return converter.convertToPresentation(value, targetType, locale);
                }

                @Override
                public Class<Integer> getModelType() {
                    return converter.getModelType();
                }

                @Override
                public Class<String> getPresentationType() {
                    return converter.getPresentationType();
                }
            });

            field.addValueChangeListener(new Property.ValueChangeListener() {
                private static final long serialVersionUID = -382717228031608542L;

                @Override
                public void valueChange(Property.ValueChangeEvent event) {

                    Property property = container.getContainerProperty(field.getData(), "amount");
                    if (property instanceof MethodProperty) {
                        ((MethodProperty) property).fireValueChange();
                    }

                    if (callback != null) {
                        callback.refresh();
                    }
                }
            });

            return field;
        }

        return null;
    }
}
