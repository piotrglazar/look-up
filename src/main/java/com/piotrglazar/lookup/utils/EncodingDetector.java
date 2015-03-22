package com.piotrglazar.lookup.utils;

import com.google.common.base.Charsets;
import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.util.Optional;

@Component
public class EncodingDetector {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int CHUNK_SIZE = 1000;

    private final UniversalDetectorFactory factory;

    @Autowired
    public EncodingDetector(UniversalDetectorFactory factory) {
        this.factory = factory;
    }

    public Optional<Charset> detectCharset(byte[] bytes) {
        UniversalDetector universalDetector = factory.getUniversalDetector();
        int arrayLength = bytes.length;
        int offset = 0;

        while (!universalDetector.isDone() && offset != arrayLength) {
            int chunkLength = calculateLength(offset, arrayLength);
            universalDetector.handleData(bytes, offset, chunkLength);
            offset += chunkLength;
        }
        universalDetector.dataEnd();

        return Optional.ofNullable(universalDetector.getDetectedCharset()).map(Charset::forName);
    }

    public boolean hasProbablyUtf8Encoding(byte[] bytes) {
        final Optional<Charset> charset = detectCharset(bytes);

        if (!charset.isPresent()) {
            LOG.info("Could not determine charset");
            return true;
        } else {
            return charset.get().equals(Charsets.UTF_8);
        }
    }

    private int calculateLength(int currentOffset, int arrayLength) {
        return Math.min(CHUNK_SIZE, arrayLength - currentOffset);
    }
}
