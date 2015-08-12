/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.components;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Container
 *
 * @author Sergey Kichenko
 * @param <T>
 * @created 06.08.2015
 */
public interface Container<T extends Serializable> {

    void addAll(Collection<T> collection);

    void add(T item);

    List<T> getSelected();

    void clear();

    void select(List<?> ids);
}
