/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging;

/**
 * MessageRequestContextHolderService
 *
 * @author Sergey Kichenko
 * @created 27.04.15 00:00
 */
public interface MessageRequestContextHolderService {

    void put(String id, MessageContext context);

    MessageContext get(String id);

    MessageContext remove(String id);

    void cleanup();
}
