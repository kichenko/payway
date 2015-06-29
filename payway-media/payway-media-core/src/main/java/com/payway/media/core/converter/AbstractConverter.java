/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.converter;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * AbstractConverter
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public abstract class AbstractConverter implements Converter, Serializable {

    private static final long serialVersionUID = 8276574043291667254L;

    protected String id;
    protected String libraryName;

    protected int defaultVideoBitRate = 250 * 1000;
    protected int defaultAudioBitRate = 64 * 1000;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getLibraryName() {
        return libraryName;
    }

    @Override
    public int getDefaultVideoBitRate() {
        return defaultVideoBitRate;
    }

    @Override
    public int getDefaultAudioBitRate() {
        return defaultAudioBitRate;
    }
}
