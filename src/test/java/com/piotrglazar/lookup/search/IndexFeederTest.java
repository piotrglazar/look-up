package com.piotrglazar.lookup.search;

import com.google.common.collect.Lists;
import com.piotrglazar.lookup.TranslationDirection;
import com.piotrglazar.lookup.configuration.FilePathResolver;
import com.piotrglazar.lookup.domain.DocumentFactory;
import com.piotrglazar.lookup.domain.LookUpDocument;
import com.piotrglazar.lookup.engine.IndexFeeder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static com.piotrglazar.lookup.TranslationDirection.POLISH_TO_ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class IndexFeederTest {

    @Mock
    private FilePathResolver filePathResolver;

    private DocumentFactory documentFactory = new DocumentFactory();

    private IndexFeeder indexFeeder;

    @Before
    public void setUp() {
        indexFeeder = new IndexFeeder(filePathResolver, documentFactory, (documents) -> documents);
    }

    @Test
    public void shouldFeedValidLinesFromSourceFile() throws IOException {
        // given
        final ClassPathResource feedResource = new ClassPathResource("indexFeeder/feed.txt");
        given(filePathResolver.getSourceFilePath()).willReturn(feedResource.getFile().toPath());

        // when
        final List<LookUpDocument> feed = indexFeeder.feed();

        // then
        assertThat(feed).hasSize(2);
        assertThatDocumentContains(feed.get(0), "this is", "an index line");
        assertThatDocumentContains(feed.get(1), "this one is", "correct too");
    }

    @Test
    public void shouldBuildDocumentFromPolishToEnglishFeed() {
        // given
        final List<String> rawDocuments = Lists.newArrayList("pies ; dog", "kot ; cat");
        final String separator = ";";
        final TranslationDirection translationDirection = POLISH_TO_ENGLISH;

        // when
        final List<LookUpDocument> feed = indexFeeder.feed(rawDocuments, separator, translationDirection);

        // then
        assertThat(feed).hasSize(2);
        assertThatDocumentContains(feed.get(0), "dog", "pies");
        assertThatDocumentContains(feed.get(1), "cat", "kot");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenUnsupportedTranslationDirectionProvided() {
        // expect
        indexFeeder.feed(Lists.newArrayList("pies ; dog"), ";", null);
    }

    private void assertThatDocumentContains(final LookUpDocument document, final String english, final String polish) {
        assertThat(document.getEnglish()).isEqualTo(english);
        assertThat(document.getPolish()).isEqualTo(polish);
    }
}
