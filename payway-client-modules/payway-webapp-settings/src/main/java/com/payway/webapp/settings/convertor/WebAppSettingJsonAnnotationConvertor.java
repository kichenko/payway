/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.settings.convertor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payway.webapp.settings.convertor.annotation.JsonWebAppSettingsConvert;
import java.util.Set;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.stereotype.Component;

/**
 * WebAppSettingJsonAnnotationConvertor
 *
 * @author Sergey Kichenko
 * @created 17.08.2015
 */
@Component(value = "app.settings.WebAppSettingJsonAnnotationConvertor")
public class WebAppSettingJsonAnnotationConvertor implements ConditionalGenericConverter {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return null;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

        try {
            if (sourceType.getType().equals(String.class) && AnnotationUtils.findAnnotation(targetType.getType(), JsonWebAppSettingsConvert.class) != null) {
                return mapper.readValue((String) source, targetType.getType());
            } else if (AnnotationUtils.findAnnotation(sourceType.getType(), JsonWebAppSettingsConvert.class) != null && targetType.getType().equals(String.class)) {
                return mapper.writeValueAsString(source);
            }
        } catch (Exception ex) {
            throw new ConversionFailedException(sourceType, targetType, source, ex);
        }

        return null;
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return (sourceType.getType().equals(String.class) && AnnotationUtils.findAnnotation(targetType.getType(), JsonWebAppSettingsConvert.class) != null)
                || (AnnotationUtils.findAnnotation(sourceType.getType(), JsonWebAppSettingsConvert.class) != null && targetType.getType().equals(String.class));
    }
}
