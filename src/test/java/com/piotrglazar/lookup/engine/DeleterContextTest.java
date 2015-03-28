package com.piotrglazar.lookup.engine;

import com.google.common.collect.Lists;
import com.piotrglazar.lookup.AbstractContextTest;
import com.piotrglazar.lookup.domain.SearchResults;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.piotrglazar.lookup.TranslationDirection.ENGLISH_TO_POLISH;
import static org.assertj.core.api.Assertions.assertThat;

public class DeleterContextTest extends AbstractContextTest {

    @Autowired
    private Deleter deleter;

    @Autowired
    private Searcher searcher;

    @Autowired
    private IndexUpdater indexUpdater;

    @Test
    public void shouldDeleteRequestedContentOnly() {
        // given
        indexUpdater.updateIndex(Lists.newArrayList("english;polish1", "english;polish2"), ";", ENGLISH_TO_POLISH);

        // when
        deleter.deleteContent("english", "polish2");

        // then
        List<SearchResults> searchResults = searcher.searchInEnglish("english");
        assertThat(searchResults).hasSize(1).extracting(SearchResults::getPolish).contains("polish1");
    }
}
