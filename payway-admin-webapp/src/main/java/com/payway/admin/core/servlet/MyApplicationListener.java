/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.core.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 *
 * @author Sergey Kichenko
 * @created 28.04.15 00:00
 */
@Slf4j
@Component
public class MyApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("#####");
    }
}
