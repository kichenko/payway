/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging.client.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.LifecycleEvent;
import com.hazelcast.core.LifecycleListener;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import com.payway.commons.webapp.bus.AppEventPublisher;
import com.payway.commons.webapp.bus.event.ConnectedClientAppEventBus;
import com.payway.commons.webapp.bus.event.DisconnectedClientAppEventBus;
import com.payway.commons.webapp.messaging.client.MessagingClient;
import com.payway.commons.webapp.messaging.client.exception.MessagingException;
import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import javax.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * MessagingClientImpl
 *
 * @author Sergey Kichenko
 * @created 01.06.15 00:00
 */
@Slf4j
@Component(value = "app.MessagingClient")
public class MessagingClientImpl implements MessagingClient, LifecycleListener {

    private HazelcastInstance client;

    @Autowired
    private TaskExecutor serverTaskExecutor;

    @Autowired
    private AppEventPublisher appEventPublisher;

    @Getter(onMethod = @_({
        @Override}))
    @Value("${app.server.queue.name}")
    private String serverQueueName;

    @Getter(onMethod = @_({
        @Override}))
    @Value("#{@'app.SimpleClientQueueNameUniqueIdGenerator'.generate()}")
    private String clientQueueName;

    @Value("${app.id}.Topic")
    private String appTopicName;

    @Value("${app.cluster.name}")
    private String clusterName;

    @Value("${app.cluster.password}")
    private String clusterPassword;

    @Value("${app.cluster.address}")
    private String[] clusterAddress;

    @Setter(AccessLevel.PRIVATE)
    private volatile MessagingClient.State state = MessagingClient.State.Stopped;

    private final MessageListener messageListener = new MessageListener() {
        @Override
        public void onMessage(Message message) {
            log.debug("Incoming message: {} [{}]", message.getMessageObject().getClass().getName(), message.getPublishingMember());
            appEventPublisher.sendNotification(message.getMessageObject());
        }
    };

    @PreDestroy
    public void preDestroy() {
        stop();
    }

    private void check() throws MessagingException {

        if (client == null) {
            throw new MessagingException("Not initialized message client instance");
        }

        if (!State.Connected.equals(getState())) {
            throw new MessagingException("Not connected message client state");
        }
    }

    @Override
    public void start() throws MessagingException {

        try {

            log.debug("Connecting client: cluster = {}, address = {}, clientQueueName = {}", clusterName, StringUtils.join(clusterAddress, ","), clientQueueName);
            if (getState() != State.Stopped) {
                throw new Exception(String.format("Cannot start client, invalid state (must be 'Stopped') - [%s]", getState()));
            }

            setState(State.Started);
            client = new MessageClientInstanceBuilder()
                    .withClusterName(clusterName)
                    .withClusterPassword(clusterPassword)
                    .withClusterAddress(clusterAddress)
                    .withConnectionAttemptLimit(0)
                    .withLifecycleListener(this)
                    .build();

            setState(State.Connected);
            client.getTopic(appTopicName).addMessageListener(messageListener);
            log.debug("Successfully message client connecting: cluster = {}, address = {}, clientQueueName = {}", clusterName, StringUtils.join(clusterAddress, ","), clientQueueName);
        } catch (Exception ex) {
            log.error("Message client connecting failed - ", ex);
            setState(State.Stopped);
            throw new MessagingException(ex.getMessage(), ex);
        }
    }

    @Override
    public void startAsync() throws MessagingException {

        serverTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    start();
                } catch (Exception ex) {
                    log.error("Async message client starting failed -", ex);
                }
            }
        });
    }

    @Override
    public void stop() {

        log.debug("Disconnecting client: cluster = {}, address = {}, clientQueueName = {}", clusterName, StringUtils.join(clusterAddress, ","), clientQueueName);
        if (client != null) {
            client.shutdown();
            setState(State.Stopped);
            client = null;
        }
    }

    @Override
    public void stateChanged(LifecycleEvent event) {

        if (LifecycleEvent.LifecycleState.SHUTDOWN.equals(event.getState())) {
            log.debug("Client shutdown: cluster = {}, address = {}, clientQueueName = {}", clusterName, StringUtils.join(clusterAddress, ","), clientQueueName);
            setState(State.Stopped);
        } else if (LifecycleEvent.LifecycleState.CLIENT_DISCONNECTED.equals(event.getState())) {
            log.debug("Client disconnected: cluster = {}, address = {}, clientQueueName = {}", clusterName, StringUtils.join(clusterAddress, ","), clientQueueName);
            setState(State.Disconnected);
            appEventPublisher.sendNotification(new DisconnectedClientAppEventBus());
        } else if (LifecycleEvent.LifecycleState.CLIENT_CONNECTED.equals(event.getState())) {
            log.debug("Client connected: cluster = {}, address = {}, clientQueueName = {}", clusterName, StringUtils.join(clusterAddress, ","), clientQueueName);
            setState(State.Connected);
            appEventPublisher.sendNotification(new ConnectedClientAppEventBus());
        }
    }

    @Override
    public boolean isConnected() {
        return MessagingClient.State.Connected.equals(getState());
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public <E extends Serializable> BlockingQueue<E> getQueue(String name) throws MessagingException {
        check();
        return client.<E>getQueue(name);
    }

    @Override
    public Lock getLock(String name) throws MessagingException {
        check();
        return client.getLock(name);
    }

    @Override
    public <E extends Serializable> BlockingQueue getClientQueue() throws MessagingException {
        return getQueue(clientQueueName);
    }

    @Override
    public <E extends Serializable> BlockingQueue getServerQueue() throws MessagingException {
        return getQueue(serverQueueName);
    }

    @Override
    public String toString() {
        return client == null ? "<empty messaging client>" : client.toString();
    }
}
