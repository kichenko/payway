/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.notification;

/**
 * NotificationService
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public interface NotificationService {
    
    void addSubscriber(Object subscriber);
    void removeSubscriber(Object subscriber);
    
    void sendNotification(NotificationEvent event);
}
