package com.payway.messaging.model;

/**
 * Created by mike on 08/07/15.
 */
public abstract class IdentifiableDto extends AbstractDto {

    final private long id;

    protected IdentifiableDto(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
