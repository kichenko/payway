package com.payway.messaging.model.common;

import com.payway.messaging.model.IdentifiableDto;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by mike on 09/06/15.
 */
// @Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class OperatorDto extends IdentifiableDto {

    private static final long serialVersionUID = -5820831317958391388L;

    private final String shortName;

    private final String name;

    private final ContentDto logo;

    public OperatorDto(long id, String shortName, String name, ContentDto logo) {
        super(id);
        this.shortName = shortName;
        this.name = name;
        this.logo = logo;
    }

    public String getShortName() {
        return shortName;
    }

    public String getName() {
        return name;
    }

    public ContentDto getLogo() {
        return logo;
    }

}
