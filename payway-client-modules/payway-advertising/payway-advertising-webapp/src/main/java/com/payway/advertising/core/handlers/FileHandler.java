/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.handlers;

/**
 * FileHandler
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
public interface FileHandler {

    /**
     *
     * @param args - in/out
     * @return
     * @throws FileHandlerException
     */
    boolean handle(FileHandlerArgs args) throws FileHandlerException;
}
