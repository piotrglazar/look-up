package com.piotrglazar.lookup.search;

import com.piotrglazar.lookup.configuration.FilePathResolver;
import org.apache.lucene.document.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class IndexFeederTest {

    @Mock
    private FilePathResolver filePathResolver;

    private DocumentFactory documentFactory = new DocumentFactory();

    private IndexFeeder indexFeeder;

    @Before
    public void setUp() throws Exception {
        indexFeeder = new IndexFeeder(filePathResolver, documentFactory);
    }

    @Test
    public void shouldFeedValidLinesFromSourceFile() throws IOException {
        // given
        final ClassPathResource classPathResource = new ClassPathResource("indexFeeder/feed.txt");
        given(filePathResolver.getSourceFilePath()).willReturn(classPathResource.getFile().toPath());

        // when
        final List<Document> feed = indexFeeder.feed();

        // then
        assertThat(feed).hasSize(2);
        assertThatDocumentContains(feed.get(0), "this is", "an index line");
        assertThatDocumentContains(feed.get(1), "this one is", "correct too");
    }

    private void assertThatDocumentContains(final Document document, final String english, final String polish) {
        assertThat(document.get("english")).isEqualTo(english);
        assertThat(document.get("polish")).isEqualTo(polish);
    }
}
