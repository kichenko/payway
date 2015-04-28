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

        System.out.println("Загрузка серверного приложения...");

        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        TaskExecutor serverTaskExecutor = context.getBean("serverTaskExecutor", TaskExecutor.class);
        MessageServerRequestListener messageServerListener = context.getBean("messageServerListener", MessageServerRequestListener.class);


        System.out.println("$$$$$$$$$$$$$$$=" + context.getBean("serverQueueName", String.class));

        serverTaskExecutor.execute(messageServerListener);

        System.out.println("Начало работы серверного приложения, для завершения работы нажмите Enter...");
        System.in.read();
    }
}
