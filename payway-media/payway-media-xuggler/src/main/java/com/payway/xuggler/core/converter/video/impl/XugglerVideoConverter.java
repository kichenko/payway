/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.xuggler.core.converter.video.impl;

import com.payway.media.core.attributes.audio.AudioAttributes;
import com.payway.media.core.attributes.video.VideoAttributes;
import com.payway.media.core.container.FormatContainer;
import com.payway.media.core.converter.video.AbstractVideoConverter;
import com.payway.media.core.exception.MediaException;
import com.xuggle.xuggler.Configuration;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IAudioResampler;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.io.URLProtocolManager;
import java.io.File;
import java.util.List;
import java.util.Properties;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * XugglerVideoConverter
 *
 * Based on
 * https://github.com/artclarke/xuggle-xuggler/blob/master/src/com/xuggle/xuggler/Converter.java
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
@Slf4j
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class XugglerVideoConverter extends AbstractVideoConverter {

    private static final long serialVersionUID = -3249771502417079729L;

    static {
        /**
         * Forces the FFMPEG io library to be loaded which means we can bypass
         * FFMPEG's file io if needed
         */
        URLProtocolManager.getManager();
    }

    /**
     * A container we'll use to read data from.
     */
    private IContainer mIContainer = null;
    /**
     * A container we'll use to write data from.
     */
    private IContainer mOContainer = null;

    /**
     * A set of {@link IStream} values for each stream in the input
     * {@link IContainer}.
     */
    private IStream[] mIStreams = null;
    /**
     * A set of {@link IStreamCoder} objects we'll use to decode audio and
     * video.
     */
    private IStreamCoder[] mICoders = null;

    /**
     * A set of {@link IStream} objects for each stream we'll output to the
     * output {@link IContainer}.
     */
    private IStream[] mOStreams = null;
    /**
     * A set of {@link IStreamCoder} objects we'll use to encode audio and
     * video.
     */
    private IStreamCoder[] mOCoders = null;

    /**
     * A set of {@link IVideoPicture} objects that we'll use to hold decoded
     * video data.
     */
    private IVideoPicture[] mIVideoPictures = null;
    /**
     * A set of {@link IVideoPicture} objects we'll use to hold
     * potentially-resampled video data before we encode it.
     */
    private IVideoPicture[] mOVideoPictures = null;

    /**
     * A set of {@link IAudioSamples} objects we'll use to hold decoded audio
     * data.
     */
    private IAudioSamples[] mISamples = null;
    /**
     * A set of {@link IAudioSamples} objects we'll use to hold
     * potentially-resampled audio data before we encode it.
     */
    private IAudioSamples[] mOSamples = null;

    /**
     * A set of {@link IAudioResampler} objects (one for each stream) we'll use
     * to resample audio if needed.
     */
    private IAudioResampler[] mASamplers = null;
    /**
     * A set of {@link IVideoResampler} objects (one for each stream) we'll use
     * to resample video if needed.
     */
    private IVideoResampler[] mVSamplers = null;

    /**
     * Should we force an interleaving of the output
     */
    private final boolean mForceInterleave = true;

    @Override
    public void convert(File inputFile, File outputFile, FormatContainer formatContainer, VideoAttributes videoAttributes, AudioAttributes audioAttributes, Properties cpreset) throws MediaException {

        int retval;

        /**
         * Keep some "pointers' we'll use for the audio we're working with.
         */
        IAudioSamples inSamples;
        IAudioSamples outSamples;
        IAudioSamples reSamples;

        /**
         * And keep some convenience pointers for the specific stream we're
         * working on for a packet.
         */
        IStreamCoder ic;
        IStreamCoder oc;
        IAudioResampler as;
        IVideoResampler vs;
        IVideoPicture inFrame;
        IVideoPicture reFrame;

        if (inputFile == null || !inputFile.exists() || !inputFile.isFile()) {
            throw new MediaException("Invalid input file");
        }

        if (outputFile == null || (outputFile.exists() && !outputFile.isFile())) {
            throw new MediaException("Invalid output file");
        }

        if (formatContainer == null || StringUtils.isBlank(formatContainer.getId())) {
            throw new MediaException("Invalid format container");
        }

        if (log.isDebugEnabled()) {
            log.debug("Start coverting video file - {}", inputFile.getAbsolutePath());
        }

        /**
         * Setup all our input and outputs
         */
        setupStreams(inputFile, outputFile, formatContainer, videoAttributes, audioAttributes, cpreset);

        /**
         * Create packet buffers for reading data from and writing data to the
         * conatiners.
         */
        IPacket iPacket = IPacket.make();
        IPacket oPacket = IPacket.make();

        /**
         * Now, we've already opened the files. We just keep reading packets
         * from it until the IContainer returns <0
         */
        while (mIContainer.readNextPacket(iPacket) == 0) {

            if (log.isDebugEnabled()) {
                log.debug("Read packet from input container...");
            }

            /**
             * Find out which stream this packet belongs to.
             */
            if (log.isDebugEnabled()) {
                log.debug("Find out which stream this packet belongs to");
            }

            int i = iPacket.getStreamIndex();
            int offset = 0;

            /**
             * Find out if this stream has a starting timestamp
             */
            if (log.isDebugEnabled()) {
                log.debug("Find out if this stream has a starting timestamp");
            }

            long tsOffset = 0;
            IStream stream = mIContainer.getStream(i);
            if (stream.getStartTime() != Global.NO_PTS && stream.getStartTime() > 0 && stream.getTimeBase() != null) {
                IRational defTimeBase = IRational.make(1, (int) Global.DEFAULT_PTS_PER_SECOND);
                tsOffset = defTimeBase.rescale(stream.getStartTime(), stream.getTimeBase());
            }

            /**
             * And look up the appropriate objects that are working on that
             * stream.
             */
            ic = mICoders[i];
            oc = mOCoders[i];
            as = mASamplers[i];
            vs = mVSamplers[i];
            inFrame = mIVideoPictures[i];
            reFrame = mOVideoPictures[i];
            inSamples = mISamples[i];
            reSamples = mOSamples[i];

            // we didn't set up this coder; ignore the packet
            if (oc == null) {
                continue;
            }

            /**
             * Find out if the stream is audio or video.
             */
            if (log.isDebugEnabled()) {
                log.debug("Find out if the stream is audio or video");
            }

            ICodec.Type cType = ic.getCodecType();
            if (cType == ICodec.Type.CODEC_TYPE_AUDIO) {

                /**
                 * Decoding audio works by taking the data in the packet, and
                 * eating chunks from it to create decoded raw data.
                 *
                 * However, there may be more data in a packet than is needed to
                 * get one set of samples (or less), so you need to iterate
                 * through the byts to get that data.
                 *
                 * The following loop is the standard way of doing that.
                 */
                if (log.isDebugEnabled()) {
                    log.debug("Start decode audio from packet...");
                }
                while (offset < iPacket.getSize()) {

                    retval = ic.decodeAudio(inSamples, iPacket, offset);
                    if (retval <= 0) {
                        throw new MediaException("Could not decode audio.  stream: " + i);
                    }

                    if (inSamples.getTimeStamp() != Global.NO_PTS) {
                        inSamples.setTimeStamp(inSamples.getTimeStamp() - tsOffset);
                    }

                    log.trace("Packet:{}; samples:{}; offset:{}", new Object[]{iPacket, inSamples, tsOffset});

                    /**
                     * If not an error, the decodeAudio returns the number of
                     * bytes it consumed. We use that so the next time around
                     * the loop we get new data.
                     */
                    offset += retval;
                    int numSamplesConsumed = 0;
                    /**
                     * If as is not null then we know a resample was needed, so
                     * we do that resample now.
                     */
                    if (as != null && inSamples.getNumSamples() > 0) {
                        retval = as.resample(reSamples, inSamples, inSamples.getNumSamples());
                        outSamples = reSamples;
                    } else {
                        outSamples = inSamples;
                    }

                    /**
                     * Now that we've resampled, it's time to encode the audio.
                     *
                     * This workflow is similar to decoding; you may have more,
                     * less or just enough audio samples available to encode a
                     * packet. But you must iterate through.
                     *
                     * Unfortunately (don't ask why) there is a slight
                     * difference between encodeAudio and decodeAudio;
                     * encodeAudio returns the number of samples consumed, NOT
                     * the number of bytes. This can be confusing, and we
                     * encourage you to read the IAudioSamples documentation to
                     * find out what the difference is.
                     *
                     * But in any case, the following loop encodes the samples
                     * we have into packets.
                     */
                    while (numSamplesConsumed < outSamples.getNumSamples()) {
                        retval = oc.encodeAudio(oPacket, outSamples, numSamplesConsumed);
                        if (retval <= 0) {
                            throw new MediaException("Could not encode any audio: " + retval);
                        }
                        /**
                         * Increment the number of samples consumed, so that the
                         * next time through this loop we encode new audio
                         */
                        numSamplesConsumed += retval;
                        log.trace("Out packet:{}; samples:{}; offset:{}", new Object[]{oPacket, outSamples, tsOffset});

                        writePacket(oPacket);
                    }
                }

                if (log.isDebugEnabled()) {
                    log.debug("Finish decode audion from packet");
                }

            } else if (cType == ICodec.Type.CODEC_TYPE_VIDEO) {

                /**
                 * This encoding workflow is pretty much the same as the for the
                 * audio above.
                 *
                 * The only major delta is that encodeVideo() will always
                 * consume one frame (whereas encodeAudio() might only consume
                 * some samples in an IAudioSamples buffer); it might not be
                 * able to output a packet yet, but you can assume that you it
                 * consumes the entire frame.
                 */
                if (log.isDebugEnabled()) {
                    log.debug("Start decode video from packet...");
                }

                IVideoPicture outFrame = null;
                while (offset < iPacket.getSize()) {
                    retval = ic.decodeVideo(inFrame, iPacket, offset);
                    if (retval <= 0) {
                        throw new MediaException("Could not decode any video.  stream: " + i);
                    }

                    log.trace("Decoded vid ts: {}; pkts ts: {}", inFrame.getTimeStamp(), iPacket.getTimeStamp());
                    if (inFrame.getTimeStamp() != Global.NO_PTS) {
                        inFrame.setTimeStamp(inFrame.getTimeStamp() - tsOffset);
                    }

                    offset += retval;
                    if (inFrame.isComplete()) {
                        if (vs != null) {
                            retval = vs.resample(reFrame, inFrame);
                            if (retval < 0) {
                                throw new MediaException("Could not resample video");
                            }
                            outFrame = reFrame;
                        } else {
                            outFrame = inFrame;
                        }

                        //outFrame.setQuality(0);
                        retval = oc.encodeVideo(oPacket, outFrame, 0);
                        if (retval < 0) {
                            throw new MediaException("Could not encode video");
                        }
                        writePacket(oPacket);
                    }
                }

                if (log.isDebugEnabled()) {
                    log.debug("Finish decode video from packet");
                }

            } else {
                /**
                 * Just to be complete; there are other types of data that can
                 * show up in streams (e.g. SUB TITLE).
                 *
                 * Right now we don't support decoding and encoding that data,
                 * but youc could still decide to write out the packets if you
                 * wanted.
                 */

                if (log.isDebugEnabled()) {
                    log.debug("Ignoring packet of type: {}", cType);
                }
            }
        }

        // and cleanup.
        closeStreams();

        if (log.isDebugEnabled()) {
            log.debug("Finish coverting video file - {}", inputFile.getAbsolutePath());
        }
    }

    /**
     * Open an initialize all Xuggler objects needed to encode and decode a
     * video file.
     */
    private int setupStreams(File inputFile, File outputFile, FormatContainer formatContainer, VideoAttributes videoAttributes, AudioAttributes audioAttributes, Properties cpreset) throws MediaException {

        int retval;

        String inputURL = inputFile.getAbsolutePath();
        String outputURL = outputFile.getAbsolutePath();

        String containerFormat = formatContainer.getId();

        String acodec = audioAttributes.getCodec() != null ? audioAttributes.getCodec().getId() : null;
        int aquality = audioAttributes.getQuality();
        int asampleRate = audioAttributes.getSampleRate();
        int achannels = audioAttributes.getChannels();
        int abitrate = audioAttributes.getBitRate();
        int avolume = audioAttributes.getVolume();

        String vcodec = videoAttributes.getCodec() != null ? videoAttributes.getCodec().getId() : null;
        int vbitrate = videoAttributes.getBitRate();
        int vbitratetolerance = videoAttributes.getBitRateTolerance();
        int vquality = videoAttributes.getQuality();

        IContainerFormat oFmt = null;

        /**
         * Create one container for input, and one for output.
         */
        if (log.isDebugEnabled()) {
            log.debug("Make input container");
        }

        mIContainer = IContainer.make();

        if (log.isDebugEnabled()) {
            log.debug("Make output container");
        }

        mOContainer = IContainer.make();

        if (log.isDebugEnabled()) {
            log.debug("Configurate output container with cpreset's");
        }

        if (cpreset != null) {
            Configuration.configure(cpreset, mOContainer);
        }

        /**
         * Open the input container for reading.
         */
        if (log.isDebugEnabled()) {
            log.debug("Open input container for reading - {}", inputURL);
        }

        retval = mIContainer.open(inputURL, IContainer.Type.READ, null);
        if (retval < 0) {
            throw new MediaException("Could not open: " + inputURL);
        }

        /**
         * If the user EXPLICITLY asked for a output container format, we'll try
         * to honor their request here.
         */
        if (log.isDebugEnabled()) {
            log.debug("Set format for output container - {}", containerFormat);
        }

        if (containerFormat != null) {
            oFmt = IContainerFormat.make();
            /**
             * Try to find an output format based on what the user specified, or
             * failing that, based on the outputURL (e.g. if it ends in .flv,
             * we'll guess FLV).
             */
            retval = oFmt.setOutputFormat(containerFormat, outputURL, null);
            if (retval < 0) {
                throw new MediaException("Could not find output container format: " + containerFormat);
            }
        }

        /**
         * Open the output container for writing. If oFmt is null, we are
         * telling Xuggler to guess the output container format based on the
         * outputURL.
         */
        if (log.isDebugEnabled()) {
            log.debug("Open the output container for writing");
        }

        retval = mOContainer.open(outputURL, IContainer.Type.WRITE, oFmt);
        if (retval < 0) {
            throw new MediaException("Could not open output url: " + outputURL);
        }

        /**
         * Find out how many streams are there in the input container? For
         * example, most FLV files will have 2 -- 1 audio stream and 1 video
         * stream.
         */
        if (log.isDebugEnabled()) {
            log.debug("Count input container streams");
        }

        int numStreams = mIContainer.getNumStreams();
        if (numStreams <= 0) {
            throw new MediaException("not streams in input url: " + inputURL);
        }

        if (log.isDebugEnabled()) {
            log.debug("Finded input container streams - {}", numStreams);
        }

        /**
         * Here we create IStream, IStreamCoders and other objects for each
         * input stream.
         *
         * We make parallel objects for each output stream as well.
         */
        mIStreams = new IStream[numStreams];
        mICoders = new IStreamCoder[numStreams];
        mOStreams = new IStream[numStreams];
        mOCoders = new IStreamCoder[numStreams];
        mASamplers = new IAudioResampler[numStreams];
        mVSamplers = new IVideoResampler[numStreams];
        mIVideoPictures = new IVideoPicture[numStreams];
        mOVideoPictures = new IVideoPicture[numStreams];
        mISamples = new IAudioSamples[numStreams];
        mOSamples = new IAudioSamples[numStreams];

        /**
         * Now let's go through the input streams one by one and explicitly set
         * up our contexts.
         */
        if (log.isDebugEnabled()) {
            log.debug("Iterating by found input container streams");
        }

        for (int i = 0; i < numStreams; i++) {

            /**
             * Get the IStream for this input stream.
             */
            if (log.isDebugEnabled()) {
                log.debug("Get input stream #{}", i);
            }

            IStream is = mIContainer.getStream(i);

            /**
             * And get the input stream coder. Xuggler will set up all sorts of
             * defaults on this StreamCoder for you (such as the audio sample
             * rate) when you open it.
             *
             * You can create IStreamCoders yourself using
             * IStreamCoder#make(IStreamCoder.Direction), but then you have to
             * set all parameters yourself.
             */
            if (log.isDebugEnabled()) {
                log.debug("Get input stream #{} coder", i);
            }

            IStreamCoder ic = is.getStreamCoder();

            /**
             * Find out what Codec Xuggler guessed the input stream was encoded
             * with.
             */
            if (log.isDebugEnabled()) {
                log.debug("Get input stream #{} codec type", i);
            }

            ICodec.Type cType = ic.getCodecType();

            mIStreams[i] = is;
            mICoders[i] = ic;
            mOStreams[i] = null;
            mOCoders[i] = null;
            mASamplers[i] = null;
            mVSamplers[i] = null;
            mIVideoPictures[i] = null;
            mOVideoPictures[i] = null;
            mISamples[i] = null;
            mOSamples[i] = null;

            if (cType == ICodec.Type.CODEC_TYPE_AUDIO) {

                /**
                 * First, did the user specify an audio codec?
                 */
                ICodec codec;
                if (acodec != null) {
                    /**
                     * Looks like they did specify one; let's look it up by
                     * name.
                     */

                    if (log.isDebugEnabled()) {
                        log.debug("Finding (by name - {}) encoding codec for audio stream", acodec);
                    }

                    codec = ICodec.findEncodingCodecByName(acodec);
                    if (codec == null || codec.getType() != cType) {
                        throw new MediaException("Could not find audio encoder: " + acodec);
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("Found encoding codec for audio stream - {}", codec.getName());
                    }
                } else {
                    /**
                     * Looks like the user didn't specify an output coder for
                     * audio.
                     *
                     * So we ask Xuggler to guess an appropriate output coded
                     * based on the URL, container format, and that it's audio.
                     */

                    if (log.isDebugEnabled()) {
                        log.debug("Guessing encoding codec for audio stream");
                    }

                    codec = ICodec.guessEncodingCodec(oFmt, null, outputURL, null, cType);
                    if (codec == null) {
                        throw new MediaException("could not guess " + cType + " encoder for: " + outputURL);
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("Guessed encoding codec for audio stream - {}", codec.getName());
                    }
                }

                /**
                 * So it looks like this stream as an audio stream. Now we add
                 * an audio stream to the output container that we will use to
                 * encode our resampled audio.
                 */
                if (log.isDebugEnabled()) {
                    log.debug("Adding new audio stream #{} to output container", i);
                }

                IStream os = mOContainer.addNewStream(codec);

                /**
                 * And we ask the IStream for an appropriately configured
                 * IStreamCoder for output.
                 *
                 * Unfortunately you still need to specify a lot of things for
                 * outputting (because we can't really guess what you want to
                 * encode as).
                 */
                IStreamCoder oc = os.getStreamCoder();

                mOStreams[i] = os;
                mOCoders[i] = oc;

                if (log.isDebugEnabled()) {
                    log.debug("Set up sample format for added audio stream #{}", i);
                }

                /**
                 * Now let's see if the codec can support the input sample
                 * format; if not we pick the last sample format the codec
                 * supports.
                 */
                IAudioSamples.Format preferredFormat = ic.getSampleFormat();

                List<IAudioSamples.Format> formats = codec.getSupportedAudioSampleFormats();
                for (IAudioSamples.Format format : formats) {
                    oc.setSampleFormat(format);
                    if (format == preferredFormat) {
                        break;
                    }
                }

                if (log.isDebugEnabled()) {
                    log.debug("Success set up sample format for added audio stream #{}", i);
                }

                if (audioAttributes.getCpreset() != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Set up cpreset for added audio stream #{}", i);
                    }
                    Configuration.configure(audioAttributes.getCpreset(), oc);
                }

                /**
                 * In general a IStreamCoder encoding audio needs to know: 1) A
                 * ICodec to use. 2) The sample rate and number of channels of
                 * the audio. Most everything else can be defaulted.
                 */
                /**
                 * If the user didn't specify a sample rate to encode as, then
                 * just use the same sample rate as the input.
                 */
                if (asampleRate == 0) {
                    asampleRate = ic.getSampleRate();
                }

                if (log.isDebugEnabled()) {
                    log.debug("Set up sample rate [{}] for added audio stream #{}", asampleRate, i);
                }
                oc.setSampleRate(asampleRate);

                /**
                 * If the user didn't specify a bit rate to encode as, then just
                 * use the same bit as the input.
                 */
                if (abitrate == 0) {
                    abitrate = ic.getBitRate();
                }

                // some containers don't give a bit-rate
                if (abitrate == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("Containers don't give a bit-rate for added audio stream #{}", i);
                    }
                    //abitrate = 64000;
                    abitrate = getDefaultAudioBitRate();
                }

                if (avolume != 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("Set up volume [{}] for added audio stream #{}", abitrate, i);
                    }
                    oc.setProperty("volume", avolume);
                }

                if (log.isDebugEnabled()) {
                    log.debug("Set up bit rate [{}] for added audio stream #{}", abitrate, i);
                }
                oc.setBitRate(abitrate);

                /**
                 * If the user didn't specify the number of channels to encode
                 * audio as, just assume we're keeping the same number of
                 * channels.
                 */
                if (achannels == 0) {
                    achannels = ic.getChannels();
                }

                if (log.isDebugEnabled()) {
                    log.debug("Set up channels [{}] for added audio stream #{}", achannels, i);
                }
                oc.setChannels(achannels);

                /**
                 * And set the quality (which defaults to 0, or highest, if the
                 * user doesn't tell us one).
                 */
                if (log.isDebugEnabled()) {
                    log.debug("Set up global quality [{}] for added audio stream #{}", aquality, i);
                }
                oc.setGlobalQuality(aquality);

                /**
                 * Now check if our output channels or sample rate differ from
                 * our input channels or sample rate.
                 *
                 * If they do, we're going to need to resample the input audio
                 * to be in the right format to output.
                 */
                if (log.isDebugEnabled()) {
                    log.debug("Check if our output channels or sample rate differ from our input channels or sample rate added audio stream #{}", i);
                }

                if (oc.getChannels() != ic.getChannels() || oc.getSampleRate() != ic.getSampleRate() || oc.getSampleFormat() != ic.getSampleFormat()) {

                    /**
                     * Create an audio resampler to do that job.
                     */
                    if (log.isDebugEnabled()) {
                        log.debug("Set up resampler for added audio stream #{}", i);
                    }

                    mASamplers[i] = IAudioResampler.make(oc.getChannels(), ic.getChannels(), oc.getSampleRate(), ic.getSampleRate(), oc.getSampleFormat(), ic.getSampleFormat());
                    if (mASamplers[i] == null) {
                        throw new MediaException("Could not open audio resampler for stream: " + i);
                    }
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("No need resampler for added audio stream #{}", i);
                    }
                    mASamplers[i] = null;
                }

                /**
                 * Finally, create some buffers for the input and output audio
                 * themselves.
                 *
                 * We'll use these repeated during the #run(CommandLine) method.
                 */
                mISamples[i] = IAudioSamples.make(1024, ic.getChannels(), ic.getSampleFormat());
                mOSamples[i] = IAudioSamples.make(1024, oc.getChannels(), oc.getSampleFormat());

                if (log.isDebugEnabled()) {
                    log.debug("Finish set up new audio stream #{}", i);
                }

            } else if (cType == ICodec.Type.CODEC_TYPE_VIDEO) {

                /**
                 * If you're reading these commends, this does the same thing as
                 * the above branch, only for video. I'm going to assume you
                 * read those comments and will only document something
                 * substantially different here.
                 */
                ICodec codec;
                if (vcodec != null) {

                    if (log.isDebugEnabled()) {
                        log.debug("Finding (by name - {}) encoding codec for video stream", acodec);
                    }

                    codec = ICodec.findEncodingCodecByName(vcodec);
                    if (codec == null || codec.getType() != cType) {
                        throw new MediaException("Could not find video encoder: " + vcodec);
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("Found encoding codec for video stream - {}", codec.getName());
                    }

                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Guessing encoding codec for video stream");
                    }

                    codec = ICodec.guessEncodingCodec(oFmt, null, outputURL, null, cType);
                    if (codec == null) {
                        throw new MediaException("Could not guess " + cType + " encoder for: " + outputURL);
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("Guessed encoding codec for video stream - {}", codec.getName());
                    }
                }

                final IStream os = mOContainer.addNewStream(codec);
                final IStreamCoder oc = os.getStreamCoder();

                mOStreams[i] = os;
                mOCoders[i] = oc;

                // Set options AFTER selecting codec
                if (videoAttributes.getCpreset() != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Set up cpreset for added video stream #{}", i);
                    }
                    Configuration.configure(videoAttributes.getCpreset(), oc);
                }

                /**
                 * In general a IStreamCoder encoding video needs to know: 1) A
                 * ICodec to use. 2) The Width and Height of the Video 3) The
                 * pixel format (e.g. IPixelFormat.Type#YUV420P) of the video
                 * data. Most everything else can be defaulted.
                 */
                if (vbitrate == 0) {
                    vbitrate = ic.getBitRate();
                }
                if (vbitrate == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("Containers don't give a bit-rate for added video stream #{}", i);
                    }
                    //vbitrate = 250000;
                    vbitrate = getDefaultVideoBitRate();
                }

                if (log.isDebugEnabled()) {
                    log.debug("Set up bit rate [{}] for added video stream #{}", vbitrate, i);
                }
                oc.setBitRate(vbitrate);

                if (vbitratetolerance > 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("Set up  bit rate tolerance [{}] for added video stream #{}", vbitratetolerance, i);
                    }
                    oc.setBitRateTolerance(vbitratetolerance);
                }

                int oWidth = ic.getWidth();
                int oHeight = ic.getHeight();

                if (log.isDebugEnabled()) {
                    log.debug("Get width [{}], height [{}] for added video stream #{}", oWidth, oHeight, i);
                }

                if (oHeight <= 0 || oWidth <= 0) {
                    throw new MediaException("Could not find width or height in url: " + inputURL);
                }

                /**
                 * For this program we don't allow the user to specify the pixel
                 * format type; we force the output to be the same as the input.
                 */
                if (log.isDebugEnabled()) {
                    log.debug("Set up  pixel type for added video stream #{}", i);
                }
                oc.setPixelType(ic.getPixelType());

                if (videoAttributes.getSize() != null && (videoAttributes.getSize().getWidth() != oWidth || videoAttributes.getSize().getHeight() != oHeight)) {
                    /**
                     * In this case, it looks like the output video requires
                     * rescaling, so we create a IVideoResampler to do that
                     * dirty work.
                     */

                    if (log.isDebugEnabled()) {
                        log.debug("Set up resampler for added video stream #{}", i);
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("Set up video size({}x{}) for added video stream #{}", videoAttributes.getSize().getWidth(), videoAttributes.getSize().getHeight(), i);
                    }

                    oWidth = videoAttributes.getSize().getWidth();
                    oHeight = videoAttributes.getSize().getHeight();

                    mVSamplers[i] = IVideoResampler.make(oWidth, oHeight, oc.getPixelType(), ic.getWidth(), ic.getHeight(), ic.getPixelType());
                    if (mVSamplers[i] == null) {
                        throw new MediaException("This version of Xuggler does not support video resampling " + i);
                    }
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("No need resampler for added video stream #{}", i);
                    }
                    mVSamplers[i] = null;
                }

                oc.setHeight(oHeight);
                oc.setWidth(oWidth);

                if (vquality >= 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("Set up global quality [{}] for added video stream #{}", aquality, i);
                    }

                    //oc.setFlag(IStreamCoder.Flags.FLAG_QSCALE, true);
                    oc.setGlobalQuality(vquality);
                }

                /**
                 * TimeBases are important, especially for Video. In general
                 * Audio encoders will assume that any new audio happens
                 * IMMEDIATELY after any prior audio finishes playing. But for
                 * video, we need to make sure it's being output at the right
                 * rate.
                 *
                 * In this case we make sure we set the same time base as the
                 * input, and then we don't change the time stamps of any
                 * IVideoPictures.
                 *
                 * But take my word that time stamps are tricky, and this only
                 * touches the envelope. The good news is, it's easier in
                 * Xuggler than some other systems.
                 */
                IRational num;
                if (videoAttributes.getFrameRate() != 0) {
                    num = IRational.make(1, videoAttributes.getFrameRate());
                } else {
                    num = ic.getFrameRate();
                }

                oc.setFrameRate(num);
                oc.setTimeBase(IRational.make(num.getDenominator(), num.getNumerator()));
                num = null; //clean, for gc

                /**
                 * And allocate buffers for us to store decoded and resample
                 * video pictures.
                 */
                mIVideoPictures[i] = IVideoPicture.make(ic.getPixelType(), ic.getWidth(), ic.getHeight());
                mOVideoPictures[i] = IVideoPicture.make(oc.getPixelType(), oc.getWidth(), oc.getHeight());

                if (log.isDebugEnabled()) {
                    log.debug("Finish set up new video stream #{}", i);
                }
            } else {
                log.warn("Ignoring input stream {} of type {}", i, cType);
            }

            /**
             * Now, once you've set up all the parameters on the StreamCoder,
             * you must open() them so they can do work.
             *
             * They will return an error if not configured correctly, so we
             * check for that here.
             */
            if (mOCoders[i] != null) {

                if (log.isDebugEnabled()) {
                    log.debug("Pre set up coders for stream #{}", i);
                }

                // some codecs require experimental mode to be set, and so we set it here.
                retval = mOCoders[i].setStandardsCompliance(IStreamCoder.CodecStandardsCompliance.COMPLIANCE_EXPERIMENTAL);
                if (retval < 0) {
                    throw new MediaException("Could not set compliance mode to experimental");
                }

                if (log.isDebugEnabled()) {
                    log.debug("Open output coders for stream #{}", i);
                }

                retval = mOCoders[i].open(null, null);
                if (retval < 0) {
                    throw new MediaException("Could not open output encoder for stream: " + i);
                }

                if (log.isDebugEnabled()) {
                    log.debug("Open input coders for stream #{}", i);
                }

                retval = mICoders[i].open(null, null);
                if (retval < 0) {
                    throw new MediaException("Could not open input decoder for stream: " + i);
                }
            }
        }

        /**
         * Pretty much every output container format has a header they need
         * written, so we do that here.
         *
         * You must configure your output IStreams correctly before writing a
         * header, and few formats deal nicely with key parameters changing
         * (e.g. video width) after a header is written.
         */
        if (log.isDebugEnabled()) {
            log.debug("Write header to output container");
        }
        retval = mOContainer.writeHeader();
        if (retval < 0) {
            throw new MediaException("Could not write header for: " + outputURL);
        }

        /**
         * That's it with setup; we're good to begin!
         */
        if (log.isDebugEnabled()) {
            log.debug("Finished creating and set up all [{}] streams", numStreams);
        }

        return numStreams;
    }

    /**
     * Close and release all resources we used to run this program.
     */
    void closeStreams() throws MediaException {

        int i;
        int numStreams;

        if (log.isDebugEnabled()) {
            log.debug("Start cleanup resources");
        }

        numStreams = mIContainer.getNumStreams();

        /**
         * Some video coders (e.g. MP3) will often "read-ahead" in a stream and
         * keep extra data around to get efficient compression. But they need
         * some way to know they're never going to get more data. The convention
         * for that case is to pass null for the IMediaData (e.g. IAudioSamples
         * or IVideoPicture) in encodeAudio(...) or encodeVideo(...) once before
         * closing the coder.
         *
         * In that case, the IStreamCoder will flush all data.
         */
        if (log.isDebugEnabled()) {
            log.debug("Flushing all streams data");
        }
        for (i = 0; i < numStreams; i++) {

            if (mOCoders[i] != null) {
                IPacket oPacket = IPacket.make();
                do {
                    if (mOCoders[i].getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
                        mOCoders[i].encodeAudio(oPacket, null, 0);
                    } else {
                        mOCoders[i].encodeVideo(oPacket, null, 0);
                    }
                    if (oPacket.isComplete()) {
                        mOContainer.writePacket(oPacket, mForceInterleave);
                    }
                } while (oPacket.isComplete());
            }
        }

        /**
         * Some container formats require a trailer to be written to avoid a
         * corrupt files.
         *
         * Others, such as the FLV container muxer, will take a writeTrailer()
         * call to tell it to seek() back to the start of the output file and
         * write the (now known) duration into the Meta Data.
         *
         * So trailers are required. In general if a format is a streaming
         * format, then the writeTrailer() will never seek backwards.
         *
         * Make sure you don't close your codecs before you write your trailer,
         * or we'll complain loudly and not actually write a trailer.
         */
        if (log.isDebugEnabled()) {
            log.debug("Write trailer to output container");
        }

        int retval = mOContainer.writeTrailer();
        if (retval < 0) {
            throw new MediaException("Could not write trailer to output file");
        }

        /**
         * We do a nice clean-up here to show you how you should do it.
         *
         * That said, Xuggler goes to great pains to clean up after you if you
         * forget to release things. But still, you should be a good boy or
         * giral and clean up yourself.
         */
        if (log.isDebugEnabled()) {
            log.debug("Clean up all streams");
        }

        for (i = 0; i < numStreams; i++) {

            if (mOCoders[i] != null) {
                /**
                 * And close the input coder to tell Xuggler it can release all
                 * native memory.
                 */

                if (log.isDebugEnabled()) {
                    log.debug("Close output coder for streams #{}", i);
                }
                mOCoders[i].close();
            }

            /**
             * Close the input coder to tell Xuggler it can release all native
             * memory.
             */
            mOCoders[i] = null;
            if (mICoders[i] != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Close input coder for streams #{}", i);
                }
                mICoders[i].close();
            }

            mICoders[i] = null;
        }

        /**
         * Tell Xuggler it can close the output file, write all data, and free
         * all relevant memory.
         */
        if (log.isDebugEnabled()) {
            log.debug("Close input container");
        }
        mOContainer.close();

        /**
         * And do the same with the input file.
         */
        if (log.isDebugEnabled()) {
            log.debug("Close output container");
        }
        mIContainer.close();

        /**
         * Technically setting everything to null here doesn't do anything but
         * tell Java it can collect the memory it used.
         *
         * The interesting thing to note here is that if you forget to close() a
         * Xuggler object, but also loose all references to it from Java, you
         * won't leak the native memory. Instead, we'll clean up after you, but
         * we'll complain LOUDLY in your logs, so you really don't want to do
         * that.
         */
        mOContainer = null;
        mIContainer = null;
        mISamples = null;
        mOSamples = null;
        mIVideoPictures = null;
        mOVideoPictures = null;
        mOCoders = null;
        mICoders = null;
        mASamplers = null;
        mVSamplers = null;

        if (log.isDebugEnabled()) {
            log.debug("Finish cleanup resources");
        }
    }

    private void writePacket(IPacket oPacket) throws MediaException {

        int retval;
        if (oPacket.isComplete()) {

            /**
             * If we got a complete packet out of the encoder, then go ahead and
             * write it to the container.
             */
            if (log.isDebugEnabled()) {
                log.debug("Write packet to ouput container");
            }
            retval = mOContainer.writePacket(oPacket, mForceInterleave);
            if (retval < 0) {
                throw new MediaException("Could not write output packet");
            }
        }
    }
}
