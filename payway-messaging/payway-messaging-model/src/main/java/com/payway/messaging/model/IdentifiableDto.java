package com.payway.messaging.model;

/**
 * Created by mike on 08/07/15.
 */
public abstract class IdentifiableDto extends AbstractDto {

    private static final long serialVersionUID = 5143947813111476369L;

    final private long id;

    protected IdentifiableDto(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
