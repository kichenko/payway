package com.payway.messaging.message.common;

import com.payway.messaging.core.response.SuccessResponse;
import lombok.*;

/**
 * Created by mike on 15/06/15.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = "content")
public class TransactionReceiptResponse implements SuccessResponse {

    private static final long serialVersionUID = 8929794225284896256L;

    private String label;

    private String contentType;

    private byte[] content;

}
