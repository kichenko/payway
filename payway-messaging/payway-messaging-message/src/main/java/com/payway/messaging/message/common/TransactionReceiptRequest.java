package com.payway.messaging.message.common;

import com.payway.messaging.core.request.command.CommandRequest;
import com.payway.messaging.message.IWebAppSessionAware;
import lombok.*;

/**
 * Created by mike on 15/06/15.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TransactionReceiptRequest extends CommandRequest implements IWebAppSessionAware {

    private static final long serialVersionUID = -1607161871013033882L;

    private String sessionId;

    private long txId;

}
