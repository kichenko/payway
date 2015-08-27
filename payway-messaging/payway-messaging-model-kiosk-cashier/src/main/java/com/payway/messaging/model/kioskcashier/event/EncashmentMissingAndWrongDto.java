/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.kioskcashier.event;

import com.payway.messaging.model.AbstractDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * EncashmentMissingAndWrongDto
 *
 * @author Sergey Kichenko
 * @created 25.08.2015
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class EncashmentMissingAndWrongDto extends AbstractDto {

    private static final long serialVersionUID = -1325642650233959362L;

    private final boolean firstTime;

    private final double totalAmountMissing;
    private final double totalAmountWrong;

    private final List<EncashmentMissingAndWrongDetailDto> missings;
    private final List<EncashmentMissingAndWrongDetailDto> wrongs;

    public EncashmentMissingAndWrongDto() {

        firstTime = false;
        totalAmountMissing = 0;
        totalAmountWrong = 0;

        missings = Collections.unmodifiableList(new ArrayList<EncashmentMissingAndWrongDetailDto>(0));
        wrongs = Collections.unmodifiableList(new ArrayList<EncashmentMissingAndWrongDetailDto>(0));
    }

    public EncashmentMissingAndWrongDto(boolean firstTime, double totalAmountMissing, double totalAmountWrong, List<EncashmentMissingAndWrongDetailDto> missings, List<EncashmentMissingAndWrongDetailDto> wrongs) {

        this.wrongs = Collections.unmodifiableList(wrongs);
        this.missings = Collections.unmodifiableList(missings);

        this.firstTime = firstTime;

        this.totalAmountMissing = totalAmountMissing;
        this.totalAmountWrong = totalAmountWrong;
    }
}
