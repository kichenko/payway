package com.payway.messaging.message;

import lombok.ToString;

/**
 * Created by mike on 20/05/15.
 */
@ToString(callSuper = true)
public class PermissionDeniedException extends RemoteException {

    private static final long serialVersionUID = -6785446579422578080L;

    public PermissionDeniedException() {
    }

    public PermissionDeniedException(String message) {
        super(message);
    }

    public PermissionDeniedException(String message, Object ... args) {
        super(String.format(message, args));
    }

    public PermissionDeniedException(Throwable cause, String message) {
        super(message, cause);
    }

    public PermissionDeniedException(Throwable cause) {
        super(cause);
    }
}
