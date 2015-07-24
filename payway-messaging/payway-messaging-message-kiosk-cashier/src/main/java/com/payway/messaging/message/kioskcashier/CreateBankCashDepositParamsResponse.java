/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.common.StaffDto;
import com.payway.messaging.model.kioskcashier.NoteCountingDepositDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * ChooseNoteCountingsNotInDepositResponse
 *
 * @author Sergey Kichenko
 * @created 21.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public final class CreateBankCashDepositParamsResponse implements SuccessResponse {

    private static final long serialVersionUID = -119808363766774281L;

    private final List<NoteCountingDepositDto> kioskEncashments;
    private final List<StaffDto> staffs;
}
