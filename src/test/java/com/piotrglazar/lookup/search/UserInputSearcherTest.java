package com.piotrglazar.lookup.search;

import com.piotrglazar.lookup.utils.ConsoleReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class UserInputSearcherTest {

    @Mock
    private ConsoleReader consoleReader;

    @Mock
    private Searcher searcher;

    @InjectMocks
    private UserInputSearcher userInputSearcher;

    @Test
    public void shouldStopWhenUserProvidedStopToken() throws IOException {
        // given
        given(consoleReader.readLine()).willReturn(UserInputSearcher.STOP);

        // when
        userInputSearcher.search();

        // then
        verifyZeroInteractions(searcher);
    }
}
