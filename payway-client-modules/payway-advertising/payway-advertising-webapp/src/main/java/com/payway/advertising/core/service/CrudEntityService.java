/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.model.DbAbstractEntity;
import java.io.Serializable;
import java.util.List;

/**
 * CrudEntityService
 *
 * @author Sergey Kichenko
 * @param <ID>
 * @param <E>
 * @created 13.05.15 00:00
 */
public interface CrudEntityService<ID extends Serializable, E extends DbAbstractEntity> {

    E save(E entity) throws ServiceException;

    void delete(E entity) throws ServiceException;

    E getById(ID id) throws ServiceException;

    List<E> list() throws ServiceException;
}
