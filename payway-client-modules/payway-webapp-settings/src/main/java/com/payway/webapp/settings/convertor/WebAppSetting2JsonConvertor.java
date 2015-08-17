/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.settings.convertor;

import com.payway.webapp.settings.model.Setting;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * WebAppSetting2JsonConvertor
 *
 * @author Sergey Kichenko
 * @created 17.08.2015
 */
@Component(value = "app.settings.WebAppSetting2JsonConvertor")
public class WebAppSetting2JsonConvertor implements Converter<Setting, String> {

    @Override
    public String convert(Setting src) {
        return null;
    }
}
