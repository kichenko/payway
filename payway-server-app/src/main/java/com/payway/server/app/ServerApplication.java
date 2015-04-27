/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.server.app;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.IdGenerator;
import com.payway.server.messaging.MessageServerRequestListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;

/**
 * Приложение, запускающее сервер обработки сообщений.
 *
 * @author Sergey Kichenko
 * @created 24.04.15 00:00
 */
public class ServerApplication {

    public static void main(String[] args) throws Exception {

        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

        HazelcastInstance hazelcastInstance = context.getBean("hazelcastInstance", HazelcastInstance.class);

        IdGenerator idGenerator = hazelcastInstance.getIdGenerator("client-server-id-generator");
        long l1 = idGenerator.newId();
        long l2 = idGenerator.newId();
        long l3 = idGenerator.newId();

        IQueue<Integer> q = hazelcastInstance.getQueue("server-queue-45");

        q.put(new Integer(1));
        q.put(new Integer(2));
        q.put(new Integer(3));
        q.put(new Integer(4));
        q.put(new Integer(5));

        TaskExecutor serverTaskExecutor = context.getBean("serverTaskExecutor", TaskExecutor.class);
        MessageServerRequestListener messageServerListener = context.getBean("messageServerListener", MessageServerRequestListener.class);
        serverTaskExecutor.execute(messageServerListener);

        System.in.read();
    }
}
