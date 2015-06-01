/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.messaging.client;

/**
 * MessageClientRecoverTask
 *
 * @author Sergey Kichenko
 * @created 01.06.15 00:00
 */
public class MessagingException extends Exception {

    private static final long serialVersionUID = 7301900566558993609L;

    public MessagingException() {
        super();
    }

    public MessagingException(String msg) {
        super(msg);
    }

    public MessagingException(String msg, Throwable th) {
        super(msg, th);
    }

}
