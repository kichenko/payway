/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.settings.convertor;

import com.payway.webapp.settings.model.Setting;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Json2WebAppSettingConvertor
 *
 * @author Sergey Kichenko
 * @created 17.08.2015
 */
@Component(value = "app.settings.Json2WebAppSettingConvertor")
public class Json2WebAppSettingConvertor implements Converter<String, Setting> {

    @Override
    public Setting convert(String source) {
        return null;
    }
}
