/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.model.configuration;

import com.payway.messaging.model.AbstractEntity;
import java.util.Collection;

/**
 * Конфигурация приложения пользователя
 *
 * @author Sergey Kichenko
 * @created 30.04.15 00:00
 */
public class Configuration extends AbstractEntity<Long> {

    private String webDavPath;
    private Collection<Item> userMenuItems;
    private Collection<Item> sideBarMenuItems;
}
