package com.payway.messaging.message.common;

import com.payway.messaging.core.request.command.CommandRequest;
import com.payway.messaging.message.IWebAppSessionAware;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by mike on 15/06/15.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TransactionReceiptRequest extends CommandRequest implements IWebAppSessionAware {

    private static final long serialVersionUID = -1607161871013033882L;

    final private String sessionId;

    final private long txId;

    public TransactionReceiptRequest(String sessionId, long txId) {
        this.sessionId = sessionId;
        this.txId = txId;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    public long getTxId() {
        return txId;
    }

}
