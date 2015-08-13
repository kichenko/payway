/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.utils;

import com.payway.webapp.reporting.transformer.factory.ComponentTransformer;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ReportComponentTransformerUtils
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Slf4j
@Component(value = "app.reporting.ReportComponentTransformerUtils")
public class ReportComponentTransformerUtils {

    @Resource(name = "app.reporting.ComponentTransformerMap")
    private Map<String, ComponentTransformer> componentTransformerMap;

    public ComponentTransformer getComponentTransformer(String key) {

        ComponentTransformer componentTransformer = componentTransformerMap.get(key.toLowerCase());
        if (componentTransformer == null) {
            log.debug("Component transformer for [{}] not found", key);
        }

        return componentTransformer;
    }
}
