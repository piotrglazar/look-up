package com.piotrglazar.lookup.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mozilla.universalchardet.UniversalDetector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EncodingDetectorTest {

    @Mock
    private UniversalDetector universalDetector;

    @Mock
    private UniversalDetectorFactory factory;

    @InjectMocks
    private EncodingDetector encodingDetector;

    @Captor
    private ArgumentCaptor<Integer> offsetCaptor;

    @Captor
    private ArgumentCaptor<Integer> lengthCaptor;

    @Before
    public void setUp() {
        given(factory.getUniversalDetector()).willReturn(universalDetector);
    }

    @Test
    public void shouldCalculateChunkLength() {
        // given
        final byte[] bytes = new byte[1400];

        // when
        encodingDetector.detectCharset(bytes);

        // then
        verify(universalDetector, times(2)).handleData(eq(bytes), offsetCaptor.capture(), lengthCaptor.capture());
        assertThat(offsetCaptor.getAllValues()).containsExactly(0, 1000);
        assertThat(lengthCaptor.getAllValues()).containsExactly(1000, 400);
    }
}
