/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging.client.impl;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
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
import lombok.NoArgsConstructor;
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

    private final Object lockObject = new Object();

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
    @Value("")
    private String clientQueueName;

    @Value("${app.client.id.generator.queue.name}")
    private String clientIdGeneratorQueueName;

    @Value("${app.client.queue.template.name}")
    private String clientQueueTemplateName;

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

    @NoArgsConstructor
    @Getter(AccessLevel.PRIVATE)
    private static final class MessageClientInstanceBuilder {

        private String clusterName;
        private String clusterPassword;
        private String[] clusterAddress;

        private int connectionTimeout;
        private int connectionAttemptLimit;
        private int connectionAttemptPeriod;

        private LifecycleListener lifecycleListener;

        public MessageClientInstanceBuilder withClusterName(String clusterName) {
            this.clusterName = clusterName;
            return this;
        }

        public MessageClientInstanceBuilder withClusterPassword(String clusterPassword) {
            this.clusterPassword = clusterPassword;
            return this;
        }

        public MessageClientInstanceBuilder withClusterAddress(String[] clusterAddress) {
            this.clusterAddress = clusterAddress;
            return this;
        }

        public MessageClientInstanceBuilder withConnectionAttemptLimit(int connectionAttemptLimit) {
            this.connectionAttemptLimit = connectionAttemptLimit;
            return this;
        }

        public MessageClientInstanceBuilder withConnectionAttemptPeriod(int connectionAttemptPeriod) {
            this.connectionAttemptPeriod = connectionAttemptPeriod;
            return this;
        }

        public MessageClientInstanceBuilder withConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public MessageClientInstanceBuilder withLifecycleListener(LifecycleListener lifecycleListener) {
            this.lifecycleListener = lifecycleListener;
            return this;
        }

        public HazelcastInstance build() {

            HazelcastInstance hzInstance;
            ClientConfig config = new ClientConfig();

            config.getGroupConfig().setName(getClusterName());
            config.getGroupConfig().setPassword(getClusterPassword());
            config.getNetworkConfig().addAddress(getClusterAddress());

            config.getNetworkConfig().setConnectionAttemptLimit(getConnectionAttemptLimit());
            config.getNetworkConfig().setConnectionAttemptPeriod(getConnectionAttemptPeriod());
            config.getNetworkConfig().setConnectionTimeout(getConnectionTimeout());

            hzInstance = HazelcastClient.newHazelcastClient(config);
            hzInstance.getLifecycleService().addLifecycleListener(lifecycleListener);

            return hzInstance;
        }
    }

    private void check() throws MessagingException {

        if (client == null) {
            throw new MessagingException("Not initialized message client instance");
        }

        if (!State.Connected.equals(getState())) {
            throw new MessagingException("Not connected message client state");
        }
    }

    private boolean start() {

        boolean flag = false;

        synchronized (lockObject) {

            try {

                log.debug("Connecting client: cluster = {}, address = {}", clusterName, StringUtils.join(clusterAddress, ","));

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

                setup();

                flag = true;

                log.debug("Successfully message client connecting: cluster = {}, address = {}, async={}", clusterName, StringUtils.join(clusterAddress, ","));
            } catch (Exception ex) {
                log.error("Message client connecting failed with exception  [{}]", ex);
                setState(State.Stopped);
            }
        }

        return flag;
    }

    @Override
    public boolean start(boolean async) {

        if (async) {
            serverTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            });
            return true;
        } else {
            return start();
        }
    }

    @Override
    public void stop() {

        synchronized (lockObject) {
            log.debug("Disconnecting client: cluster = {}, address = {}", clusterName, StringUtils.join(clusterAddress, ","));
            if (client != null) {
                client.shutdown();
                setState(State.Stopped);
                client = null;
            }
        }
    }

    @PreDestroy
    public void preDestroy() {
        stop();
    }

    @Override
    public boolean isConnected() {
        return MessagingClient.State.Connected.equals(getState());
    }

    //?
    private void setup() {
        log.debug("Client connected: cluster = {}, address = {}", clusterName, StringUtils.join(clusterAddress, ","));
        setState(State.Connected);
        client.getTopic(appTopicName).addMessageListener(messageListener);
        clientQueueName = String.format("%s%d", clientQueueTemplateName, client.getIdGenerator(clientIdGeneratorQueueName).newId());
        appEventPublisher.sendNotification(new ConnectedClientAppEventBus());
    }

    @Override
    public void stateChanged(LifecycleEvent event) {

        if (LifecycleEvent.LifecycleState.SHUTDOWN.equals(event.getState())) {
            log.debug("Client shutdown: cluster = {}, address = {}", clusterName, StringUtils.join(clusterAddress, ","));
            setState(State.Stopped);
        } else if (LifecycleEvent.LifecycleState.CLIENT_DISCONNECTED.equals(event.getState())) {
            log.debug("Client disconnected: cluster = {}, address = {}", clusterName, StringUtils.join(clusterAddress, ","));
            setState(State.Disconnected);
            appEventPublisher.sendNotification(new DisconnectedClientAppEventBus());
        } else if (LifecycleEvent.LifecycleState.CLIENT_CONNECTED.equals(event.getState())) {
            setup();
        }
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
