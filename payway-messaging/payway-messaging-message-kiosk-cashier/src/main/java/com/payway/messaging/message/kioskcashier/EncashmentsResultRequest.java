/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.message.SessionCommandRequest;
import com.payway.messaging.model.kioskcashier.BanknoteNominalEncashmentDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * EncashmentsResultRequest
 *
 * @author Sergey Kichenko
 * @created 06.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public final class EncashmentsResultRequest extends SessionCommandRequest {
    
    private static final long serialVersionUID = 2849838982105517376L;
    
    private final long kioskEncashmentId;
    private final List<BanknoteNominalEncashmentDto> encashments;
    
    public EncashmentsResultRequest(String sessionId, long kioskEncashmentId, List<BanknoteNominalEncashmentDto> encashments) {
        this(kioskEncashmentId, encashments);
        setSessionId(sessionId);
    }
}
