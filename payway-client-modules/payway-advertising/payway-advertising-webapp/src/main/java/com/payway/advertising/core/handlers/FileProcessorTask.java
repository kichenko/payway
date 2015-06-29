/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.handlers;

import java.util.List;

/**
 * FileProcessorTask
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
public interface FileProcessorTask {

    void process() throws FileProcessorException;

    List<FileHandler> getHandlers();
}
