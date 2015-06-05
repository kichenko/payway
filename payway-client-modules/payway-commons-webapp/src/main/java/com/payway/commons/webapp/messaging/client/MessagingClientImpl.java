/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.*;
import com.payway.commons.webapp.bus.AppEventPublisher;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.Serializable;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

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

    private volatile boolean construct;
    private volatile boolean shutdown;

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${server.queue.name}")
    private String serverQueueName;

    @Value("")
    private String clientQueueName;

    @Value("${client.id.generator.queue.name}")
    private String clientIdGeneratorQueueName;

    @Value("${client.queue.template.name}")
    private String clientQueueTemplateName;

    @Value("${client.config.hz:classpath:hazelcast-client.xml}")
    private Resource config;

    @Setter(AccessLevel.PRIVATE)
    public volatile IMessagingClient.State state = IMessagingClient.State.Disconnected;

    @Value("true")
    private volatile boolean autoRecover;

    @Autowired
    @Qualifier(value = "serverTaskExecutor")
    private TaskExecutor serverTaskExecutor;

    @Value("${app.id}")
    private String appTopicName;

    @PreDestroy
    public void preDestroy() {
        shutdown();
    }

    @Override
    public void construct() throws Exception {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Start constructing messaging client");
            }

            if (semaphore.tryAcquire(1, TimeUnit.SECONDS)) {
                construct = true;
                client = HazelcastClient.newHazelcastClient(new XmlClientConfigBuilder(config.getInputStream()).build());
                client.getLifecycleService().addLifecycleListener(this);
                client.getTopic(appTopicName).addMessageListener(messageListener);
                clientQueueName = String.format("%s%d", clientQueueTemplateName, client.getIdGenerator(clientIdGeneratorQueueName).newId());
                setState(IMessagingClient.State.Connected);
            } else {
                throw new Exception("Can't get permits for semaphore");
            }

            if (log.isDebugEnabled()) {
                log.debug("End constructing messaging client");
            }
        } catch (Exception ex) {
            log.error("Bad constructing messaging client", ex);
            setState(IMessagingClient.State.Disconnected);
            throw ex;
        } finally {
            construct = false;
            semaphore.release();
        }
    }

    @Override
    public void stateChanged(LifecycleEvent event) {
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

    @Autowired
    AppEventPublisher appEventPublisher;

    private MessageListener messageListener = new MessageListener() {
        @Override public void onMessage(Message message) {
            log.debug("Incoming message: %s [%s]", message.getMessageObject().getClass().getName(), message.getPublishingMember());
            appEventPublisher.sendNotification(message.getMessageObject());
        }
    };

}
