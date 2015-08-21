/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging.collector.impl;

import com.payway.commons.webapp.messaging.MessageRequestContextHolderService;
import com.payway.commons.webapp.messaging.collector.MessagingCollectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * MessagingCollectorServiceImpl
 *
 * @author Sergey Kichenko
 * @created 18.08.2015
 */
@Slf4j
@Component(value = "app.MessagingCollectorServiceImpl")
public class MessagingCollectorServiceImpl implements MessagingCollectorService {

    @Autowired
    private MessageRequestContextHolderService contextService;

    @Override
    @Scheduled(fixedDelayString = "${app.messaging.collector.fixed.delay:900000}")
    public void execute() {
        log.debug("Start message collector executing...");
        contextService.cleanup();
        log.debug("Stop message collector executing...");
    }
}
