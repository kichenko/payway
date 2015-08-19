/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging.client.impl;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.LifecycleListener;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * MessageClientInstanceBuilder
 *
 * @author Sergey Kichenko
 * @created 01.06.15 00:00
 */
@NoArgsConstructor
@Getter(AccessLevel.PRIVATE)
final class MessageClientInstanceBuilder {

    private static final int CONNECTION_TIMEOUT = 5000;
    private static final int CONNECTION_ATTEMPT_LIMIT = 2;
    private static final int CONNECTION_ATTEMPT_PERIOD = 3000;

    private String clusterName;
    private String clusterPassword;
    private String[] clusterAddress;

    private int connectionTimeout = CONNECTION_TIMEOUT;
    private int connectionAttemptLimit = CONNECTION_ATTEMPT_LIMIT;
    private int connectionAttemptPeriod = CONNECTION_ATTEMPT_PERIOD;

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
