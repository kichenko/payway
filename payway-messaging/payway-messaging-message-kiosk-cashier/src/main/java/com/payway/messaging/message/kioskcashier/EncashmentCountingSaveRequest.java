/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.message.SessionCommandRequest;
import com.payway.messaging.model.kioskcashier.BanknoteNominalEncashmentDto;
import java.util.List;
import lombok.ToString;

/**
 * EncashmentCountingSaveRequest
 *
 * @author Sergey Kichenko
 * @created 06.07.15 00:00
 */
@ToString(callSuper = true)
public final class EncashmentCountingSaveRequest extends SessionCommandRequest {
    
    private static final long serialVersionUID = 2849838982105517376L;
    
    private final long kioskEncashmentId;

    private final List<BanknoteNominalEncashmentDto> encashments;

    public EncashmentCountingSaveRequest(String sessionId, long kioskEncashmentId, List<BanknoteNominalEncashmentDto> encashments) {
        super(sessionId);
        this.kioskEncashmentId = kioskEncashmentId;
        this.encashments = encashments;
    }

    public long getKioskEncashmentId() {
        return kioskEncashmentId;
    }

    public List<BanknoteNominalEncashmentDto> getEncashments() {
        return encashments;
    }

}
