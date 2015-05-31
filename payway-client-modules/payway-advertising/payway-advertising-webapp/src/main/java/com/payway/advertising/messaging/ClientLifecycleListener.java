/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.messaging;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.impl.HazelcastClientInstanceImpl;
import com.hazelcast.client.impl.HazelcastClientProxy;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.LifecycleEvent;
import com.hazelcast.core.LifecycleListener;
import com.hazelcast.instance.OutOfMemoryErrorDispatcher;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * ClientLifecycleListener
 *
 * @author Sergey Kichenko
 * @created 31.05.15 00:00
 */
@Slf4j
@Component(value = "clientLifecycleListener")
public class ClientLifecycleListener implements LifecycleListener {

    @Autowired
    private ApplicationContext appContext;

    @Override
    public void stateChanged(LifecycleEvent event) {
        log.debug("Client event is --- {}", event);

        if (event.getState().equals(LifecycleEvent.LifecycleState.SHUTTING_DOWN)) {
            //restartHazelcastClient((HazelcastInstance) appContext.getBean("hazelcastInstance"));
        }
    }

    @PreDestroy
    public void preDestroy() {
        log.debug("Client preDestroy");
    }

    public static void restartHazelcastClient(HazelcastInstance clientInstance) {
        clientInstance.shutdown();
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(HazelcastClient.class.getClassLoader());
            HazelcastClientProxy proxy = ((HazelcastClientProxy) clientInstance);
            ClientConfig clientConfig = proxy.client.getClientConfig();
            HazelcastClientInstanceImpl client = new HazelcastClientInstanceImpl(clientConfig);
            client.start();
            OutOfMemoryErrorDispatcher.registerClient(client);
            proxy.client = client;
        } catch (Exception ex) {
            log.error("error", ex);
        } finally {
            Thread.currentThread().setContextClassLoader(tccl);
        }
    }

}
