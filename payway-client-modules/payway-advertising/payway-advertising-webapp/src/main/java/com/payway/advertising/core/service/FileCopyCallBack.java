/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.file.FileSystemObject;

/**
 * FileCopyCallBack
 *
 * @author Sergey Kichenko
 * @created 18.05.15 00:00
 */
public interface FileCopyCallBack {

    boolean copy(FileSystemObject f);

    void finish();

}
