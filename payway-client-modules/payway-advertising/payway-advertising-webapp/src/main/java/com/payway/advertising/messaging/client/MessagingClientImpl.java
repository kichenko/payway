/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.messaging.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.LifecycleEvent;
import com.hazelcast.core.LifecycleListener;
import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * MessagingClientImpl
 *
 * @author Sergey Kichenko
 * @created 01.06.15 00:00
 */
@Slf4j
@Component(value = "messagingClient")
public class MessagingClientImpl implements IMessagingClient, LifecycleListener {

    private HazelcastInstance client;

    private final Semaphore semaphore = new Semaphore(1);
    private boolean construct;
    private boolean shutdown;

    @Value("${server.queue.name}")
    private String serverQueueName;

    @Value("")
    private String clientQueueName;

    @Value("${client.id.generator.queue.name}")
    private String clientIdGeneratorQueueName;

    @Value("${client.queue.template.name}")
    private String clientQueueTemplateName;

    @Setter(AccessLevel.PRIVATE)
    public volatile IMessagingClient.State state = IMessagingClient.State.Disconnected;

    @Value("${client.messaging.config.file.name}")
    private String configFileName;

    @Value("true")
    private boolean autoRecover;

    @Autowired
    @Qualifier(value = "serverTaskExecutor")
    private TaskExecutor serverTaskExecutor;

    @Autowired
    private ApplicationContext applicationContext;

    @PreDestroy
    public void preDestroy() {
        shutdown();
    }

    @Override
    public boolean construct() {
        try {
            semaphore.tryAcquire(1, TimeUnit.SECONDS);
            construct = true;

            if (client != null) {
                client.shutdown();
                client = null;
            }

            client = HazelcastClient.newHazelcastClient(new XmlClientConfigBuilder(new File(new URI(configFileName))).build());
            client.getLifecycleService().addLifecycleListener(this);
            clientQueueName = String.format("%s%d", clientQueueTemplateName, client.getIdGenerator(clientIdGeneratorQueueName).newId());
            setState(IMessagingClient.State.Connected);

            return true;
        } catch (Exception ex) {
            log.error("Bad constructing messaging client", ex);
            setState(IMessagingClient.State.Disconnected);
        } finally {
            semaphore.release();
            construct = false;
        }

        return false;
    }

    @Override
    public void stateChanged(LifecycleEvent event) {
        log.debug("client state ==== {}", event);
        if (autoRecover && !construct && !shutdown && LifecycleEvent.LifecycleState.SHUTDOWN.equals(event.getState())) {
            setState(IMessagingClient.State.Connecting);
            try {
                serverTaskExecutor.execute((MessagingClientRecoverTask) applicationContext.getBean("messagingClientRecoverTask"));
            } catch (Exception ex) {
                log.error("Bad start recovering messaging client task", ex);
            }
        }

        if (shutdown && LifecycleEvent.LifecycleState.SHUTDOWN.equals(event.getState())) {
            shutdown = false;
        }
    }

    @Override
    public void shutdown() {
        if (client != null) {
            shutdown = true;
            setState(IMessagingClient.State.Shutdown);

            client.shutdown();
            client = null;
        }
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public <E extends Serializable> IMessagingQueue<E> getQueue(String name) throws MessagingException {
        if (client != null && IMessagingClient.State.Connected.equals(getState())) {
            return new MessagingQueueImpl<>(name, client.<E>getQueue(name), this);
        } else {
            throw new MessagingException("Bad messaging client state");
        }
    }

    @Override
    public IMessagingLock getLock(String name) throws MessagingException {
        if (client != null && IMessagingClient.State.Connected.equals(getState())) {
            return new MessagingLockImpl(client.getLock(name), this);
        } else {
            throw new MessagingException("Bad messaging client state");
        }
    }

    @Override
    public <E extends Serializable> IMessagingQueue getClientQueue() throws MessagingException {
        return getQueue(clientQueueName);
    }

    @Override
    public <E extends Serializable> IMessagingQueue getServerQueue() throws MessagingException {
        return getQueue(serverQueueName);
    }

    @Override
    public String toString() {
        if (client != null) {
            client.toString();
        }
        return "";
    }
}
