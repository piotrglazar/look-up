package com.piotrglazar.lookup.utils;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.piotrglazar.lookup.AbstractContextTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;

import static com.piotrglazar.lookup.TestUtils.classPathResource;
import static com.piotrglazar.lookup.TestUtils.fileSystemResource;
import static org.assertj.core.api.Assertions.assertThat;

public class EncodingDetectorContextTest extends AbstractContextTest {

    @Autowired
    private EncodingDetector encodingDetector;

    @Test
    public void shouldDetectUtf8Encoding() throws IOException {
        // given
        final ClassPathResource classPathResource = classPathResource("source/medic.txt");
        final byte[] bytes = ByteStreams.toByteArray(classPathResource.getInputStream());

        // when
        final Optional<Charset> charset = encodingDetector.detectCharset(bytes);

        // then
        assertThat(charset.isPresent());
        assertThat(charset.get()).isEqualTo(Charsets.UTF_8);
    }

    @Test
    public void shouldDetectWindowsEncoding() throws IOException {
        // given
        final FileSystemResource fileSystemResource = fileSystemResource("invalidGlossary.csv");
        final byte[] bytes = ByteStreams.toByteArray(fileSystemResource.getInputStream());

        // when
        final Optional<Charset> charset = encodingDetector.detectCharset(bytes);

        // then
        assertThat(charset.isPresent());
        assertThat(charset.get()).isEqualTo(Charset.forName("windows-1252"));
    }

    @Test
    public void shouldNotDetectAnyEncoding() {
        // given
        final byte[] bytes = "spring boot is cool".getBytes(Charsets.US_ASCII);

        // when
        final Optional<Charset> charset = encodingDetector.detectCharset(bytes);

        // then
        assertThat(charset.isPresent()).isFalse();
    }
}
