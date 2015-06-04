/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.messaging.client;

/**
 * IMessagingObject
 *
 * @author Sergey Kichenko
 * @created 01.06.15 00:00
 */
public interface IMessagingObject {

    IMessagingClient getMessagingClient();
    String getName();
}
