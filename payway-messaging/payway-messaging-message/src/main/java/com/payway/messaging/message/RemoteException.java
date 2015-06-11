package com.payway.messaging.message;

/**
 * Created by mike on 20/05/15.
 */
public class RemoteException extends RuntimeException {

    private static final long serialVersionUID = 4463730368938416220L;

    public RemoteException() {
        // ~~
    }

    public RemoteException(String message) {
        super(message);
    }

    public RemoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteException(Throwable cause) {
        super(cause);
    }

}
