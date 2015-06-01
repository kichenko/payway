/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.messaging.client;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * MessagingQueueImpl
 *
 * @author Sergey Kichenko
 * @param <E>
 * @created 01.06.15 00:00
 */
@Slf4j
@ToString
public class MessagingQueueImpl<E extends Serializable> implements IMessagingQueue<E> {
    
    @Setter(AccessLevel.PRIVATE)
    private String name;
    
    @Setter(AccessLevel.PRIVATE)
    private BlockingQueue<E> queue;
    
    @Setter(AccessLevel.PRIVATE)
    private IMessagingClient messagingClient;
    
    public MessagingQueueImpl() {
        //
    }
    
    public MessagingQueueImpl(String name, BlockingQueue<E> queue, IMessagingClient messagingClient) {
        setName(name);
        setQueue(queue);
        setMessagingClient(messagingClient);
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void put(E e) throws MessagingException {
        if (messagingClient != null && IMessagingClient.State.Connected.equals(messagingClient.getState())) {
            try {
                queue.put(e);
            } catch (Exception ex) {
                log.error("Bad put to messaging queue", ex);
                throw new MessagingException("Bad put to messaging queue", ex);
            }
        } else {
            log.error("Bad put to messaging queue, invalid state");
            throw new MessagingException("Bad put to messaging queue, invalid state");
        }
    }
    
    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws MessagingException {
        if (messagingClient != null && IMessagingClient.State.Connected.equals(messagingClient.getState())) {
            try {
                return queue.offer(e, timeout, unit);
            } catch (Exception ex) {
                log.error("Bad offer to messaging queue", ex);
                throw new MessagingException("Bad offer to messaging queue", ex);
            }
        } else {
            log.error("Bad offer to messaging queue, invalid state");
            throw new MessagingException("Bad offer to messaging queue, invalid state");
        }
    }
    
    @Override
    public E poll(long timeout, TimeUnit unit) throws MessagingException {
        if (messagingClient != null && IMessagingClient.State.Connected.equals(messagingClient.getState())) {
            try {
                return queue.poll(timeout, unit);
            } catch (Exception ex) {
                log.error("Bad poll to messaging queue", ex);
                throw new MessagingException("Bad poll to messaging queue", ex);
            }
        } else {
            log.error("Bad poll to messaging queue, invalid state");
            throw new MessagingException("Bad poll to messaging queue, invalid state");
        }
    }
    
    @Override
    public IMessagingClient getMessagingClient() {
        return messagingClient;
    }
}
