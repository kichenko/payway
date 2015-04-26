/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.server.controller;

import com.payway.server.messaging.MessageServerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Контроллер, запускающий сервер обработки сообщений.
 *
 * @author Sergey Kichenko
 * @created 24.04.15 00:00
 */
@RequestMapping(value = "/message-server")
public class MessageServerController {

    @Autowired
    @Qualifier("serverTaskExecutor")
    private TaskExecutor serverTaskExecutor;

    @Autowired
    @Qualifier("messageServerListener")
    private MessageServerListener messageServerListener;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        return "index";
    }

    /**
     * Запуск сервера обработки сообщений. Выделяется один поток из пула.
     */
    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public void start() {
        serverTaskExecutor.execute(messageServerListener);
    }

    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    public void stop(Model model) {
        //
    }
}
