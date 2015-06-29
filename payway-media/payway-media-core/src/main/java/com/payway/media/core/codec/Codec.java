/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.codec;

/**
 * Codec
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
public interface Codec {

    String getId();

    String getShortName();

    String getLongName();

    CodecType getType();

    CodecDirection getDirection();
}
