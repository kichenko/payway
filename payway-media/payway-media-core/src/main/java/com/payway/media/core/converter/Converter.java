/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.converter;

/**
 * Converter
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
public interface Converter {

    String getId();

    String getLibraryName();

    int getDefaultVideoBitRate();

    int getDefaultAudioBitRate();
}
