package com.payway.messaging.message;

import lombok.ToString;

/**
 * Created by mike on 20/05/15.
 */
@ToString(callSuper = true)
public class PermissionDeniedException extends RemoteException {
    public PermissionDeniedException() {
    }

    public PermissionDeniedException(String message) {
        super(message);
    }

    public PermissionDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionDeniedException(Throwable cause) {
        super(cause);
    }
}
