/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging;

import lombok.ToString;
import org.joda.time.LocalDateTime;

/**
 * MessageContextImpl
 *
 * @author Sergey Kichenko
 * @created 29.04.15 00:00
 */
@ToString
public final class MessageContextImpl implements MessageContext {

    private static final long serialVersionUID = 7871864510583276539L;

    private final long lifetime;
    private final ResponseCallBack callback;
    private final LocalDateTime created = new LocalDateTime();

    public MessageContextImpl(long lifetime, ResponseCallBack callback) {
        this.lifetime = lifetime;
        this.callback = callback;
    }

    @Override
    public ResponseCallBack getCallback() {
        return callback;
    }

    @Override
    public long getLifeTime() {
        return lifetime;
    }

    @Override
    public LocalDateTime getCreated() {
        return created;
    }

    @Override
    public boolean isExpired() {
        return created.plusMillis((int) lifetime).isAfter(LocalDateTime.now());
    }
}
