/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.server.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * PaywayServerController
 *
 * @author Sergey Kichenko
 * @created 24.04.15 00:00
 */
@RequestMapping(value = "/")
public class PaywayServerController {

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        return "index";
    }
}
