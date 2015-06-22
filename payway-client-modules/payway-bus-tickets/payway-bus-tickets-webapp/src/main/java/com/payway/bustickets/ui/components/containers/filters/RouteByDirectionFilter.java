/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.components.containers.filters;

import com.payway.messaging.model.bustickets.DirectionDto;
import com.payway.messaging.model.bustickets.RouteDto;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * RouteByDirectionFilter
 *
 * @author Sergey Kichenko
 * @created 10.06.15 00:00
 */
@Slf4j
@EqualsAndHashCode
@NoArgsConstructor
public final class RouteByDirectionFilter implements Container.Filter {

    private static final long serialVersionUID = -7291900977113521462L;

    @Setter(AccessLevel.PRIVATE)
    private DirectionDto direction;

    public RouteByDirectionFilter(DirectionDto direction) {
        setDirection(direction);
    }

    @Override
    public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {

        if (item instanceof BeanItem) {
            RouteDto route = ((BeanItem<RouteDto>) item).getBean();
            if (route != null && route.getDirection() != null) {
                return route.getDirection().equals(direction);
            }
        }

        return false;
    }

    @Override
    public boolean appliesToProperty(Object propertyId) {
        return true;
    }
}
