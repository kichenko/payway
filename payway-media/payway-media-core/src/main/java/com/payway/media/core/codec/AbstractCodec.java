/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.codec;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * AbstractCodec
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractCodec implements Codec, Serializable {

    private static final long serialVersionUID = 3550881849049505119L;

    protected String id;
    protected String shortName;
    protected String longName;
    protected CodecType codecType;
    protected CodecDirection codecDirection;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public String getLongName() {
        return longName;
    }

    @Override
    public CodecType getType() {
        return codecType;
    }

    @Override
    public CodecDirection getDirection() {
        return codecDirection;
    }
}
