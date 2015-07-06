/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.kioskcashier.BanknoteNominalEncashmentDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * EncashmentSaveBanknoteNominalSuccessResponse
 *
 * @author Sergey Kichenko
 * @created 06.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public final class EncashmentSaveBanknoteNominalSuccessResponse implements SuccessResponse {

    private static final long serialVersionUID = 4838381928224679017L;

    private final List<BanknoteNominalEncashmentDto> encashment;

}