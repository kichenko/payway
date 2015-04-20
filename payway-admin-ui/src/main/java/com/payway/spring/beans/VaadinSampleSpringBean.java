/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.spring.beans;

import org.springframework.stereotype.Component;

/**
 * VaadinSampleSpringBean
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@Component
public class VaadinSampleSpringBean {

    public String getCaption() {
        return "Thanks for clicking! (Spring bean @)";
    }
}
