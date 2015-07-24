/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.kioskcashier;

import com.payway.messaging.model.IdentifiableDto;
import java.util.Date;
import lombok.Getter;
import lombok.ToString;

/**
 * NoteCountingDepositDto
 *
 * @author Sergey Kichenko
 * @created 21.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
public final class NoteCountingDepositDto extends IdentifiableDto {

    private static final long serialVersionUID = 3568043301866630164L;

    private final Date created;
    private final String terminalName;
    private final int seqNum;

    public NoteCountingDepositDto(long id, Date created, String terminalName, int seqNum) {
        super(id);
        this.created = created;
        this.terminalName = terminalName;
        this.seqNum = seqNum;
    }
}
