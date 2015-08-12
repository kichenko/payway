/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.core.generators.impl;

import com.payway.commons.webapp.core.generators.UniqueIdGenerator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * SimpleClientQueueNameUniqueIdGenerator
 *
 * @author Sergey Kichenko
 * @created 12.08.2015
 */
@Component(value = "app.SimpleClientQueueNameUniqueIdGenerator")
public class SimpleClientQueueNameUniqueIdGenerator implements UniqueIdGenerator {

    @Value("${app.id}")
    private String appId;

    @Value("yyyyMMddHHmmss")
    private String patternDate;

    @Override
    public String generate() {
        return new StringBuilder(appId).append("-").append(UUID.randomUUID().toString()).append("-").append(new SimpleDateFormat(patternDate).format(new Date())).toString();
    }

}
