/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@Getter
@Setter
@ToString
public abstract class AbstractEnvelope implements Envelope {

    /**
     * Unique message identifier
     */
    private String messageId = UUID.randomUUID().toString();

    /**
     * The origin of the message
     */
    private String origin;

    /**
     * Timestamp of the message
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * Payload
     */
    private Message body;

    public AbstractEnvelope(String origin, Message body) {
        this.origin = origin;
        this.body = body;
    }

}
