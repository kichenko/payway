/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.config.apply;

/**
 * ApplyStatus
 *
 * @author Sergey Kichenko
 * @created 22.05.15 00:00
 */
public enum ApplyStatus {

    None,
    Prepare,
    Canceling,
    Cancel,
    CopyFiles,
    UpdateDatabase,
    Confirmation,
    Success,
    Fail
}
