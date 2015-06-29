/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.handlers;

import java.util.Map;

/**
 * FileHandler
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
public interface FileHandler {

    boolean handle(String srcFilePath, String srcFileName, Map<String, Object> params) throws FileHandlerException;
}
