package com.piotrglazar.lookup.utils;

import com.google.common.base.Charsets;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ByteArrayToStringListConverterTest {

    private ByteArrayToStringListConverter converter = new ByteArrayToStringListConverter();

    @Test
    public void shouldConvertByteArrayToString() {
        // given
        final byte[] bytes = "Java 8 is so cool\nSpring Boot is awesome, too".getBytes(Charsets.UTF_8);

        // when
        final List<String> strings = converter.convert(bytes);

        // then
        assertThat(strings).containsExactly("Java 8 is so cool", "Spring Boot is awesome, too");
    }

    @Test
    public void shouldConvertEmptyArray() {
        // given
        final byte[] bytes = new byte[0];

        // when
        final List<String> strings = converter.convert(bytes);

        // then
        assertThat(strings).containsExactly("");
    }

    @Test
    public void shouldRemoveCarriageReturnCharacter() {
        // given
        final byte[] bytes = "Java 8 is so cool\r\nSpring Boot is awesome, too".getBytes(Charsets.UTF_8);

        // when
        final List<String> strings = converter.convert(bytes);

        // then
        assertThat(strings).containsExactly("Java 8 is so cool", "Spring Boot is awesome, too");
    }
}
