/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.server.app;

import com.payway.server.messaging.MessageServerRequestListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;

/**
 * Приложение, запускающее сервер обработки сообщений.
 *
 * @author Sergey Kichenko
 * @created 24.04.15 00:00
 */
@Slf4j
public class ServerApplication {

    public static void main(String[] args) throws Exception {

        log.info("Starting server application...");

        log.info("Start load application context");
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        log.info("End load application context");

        log.info("Start running thread of processing incoming messages");
        TaskExecutor serverTaskExecutor = context.getBean("serverTaskExecutor", TaskExecutor.class);
        MessageServerRequestListener messageServerListener = context.getBean("messageServerListener", MessageServerRequestListener.class);
        serverTaskExecutor.execute(messageServerListener);
        log.info("End running thread of processing incoming messages");

        System.out.println("Server application started, press Enter to exit");
        System.in.read();
    }
}
