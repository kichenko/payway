/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.container.service;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.payway.media.core.codec.Codec;
import com.payway.media.core.codec.CodecDirection;
import com.payway.media.core.codec.CodecType;
import com.payway.media.core.container.FormatContainer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * AbstractContainerService
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public abstract class AbstractContainerService implements ContainerService, Serializable {

    private static final long serialVersionUID = -6652182573346311529L;

    protected final List<FormatContainer> supportedContainers = new ArrayList<>();
    protected final List<Codec> supportedCodecs = new ArrayList<>();

    @Override
    public List<FormatContainer> getSupportedFormatContainers() {
        return Collections.unmodifiableList(supportedContainers);
    }

    @Override
    public List<Codec> getSupportedCodecs(FormatContainer format) {
        return Collections.unmodifiableList(supportedCodecs);
    }

    @Override
    public List<Codec> getSupportedAudioEncoderCodecs(FormatContainer format) {
        return FluentIterable.from(supportedCodecs).filter(new Predicate<Codec>() {
            @Override
            public boolean apply(Codec codec) {
                return CodecDirection.Encoder.equals(codec.getDirection()) && CodecType.Audio.equals(codec.getType());
            }
        }).toList();
    }

    @Override
    public List<Codec> getSupportedVideoEncoderCodecs(FormatContainer format) {
        return FluentIterable.from(supportedCodecs).filter(new Predicate<Codec>() {
            @Override
            public boolean apply(Codec codec) {
                return CodecDirection.Encoder.equals(codec.getDirection()) && CodecType.Video.equals(codec.getType());
            }
        }).toList();
    }

    @Override
    public boolean isContainerSupported(FormatContainer container) {
        return supportedContainers.contains(container);
    }

    @Override
    public boolean isCodecSupported(Codec codec) {
        return supportedCodecs.contains(codec);
    }
}
