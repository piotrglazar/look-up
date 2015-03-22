package com.piotrglazar.lookup.search;

import com.google.common.collect.Lists;
import com.piotrglazar.lookup.AbstractContextTest;
import com.piotrglazar.lookup.domain.SearchResults;
import com.piotrglazar.lookup.engine.IndexUpdater;
import org.assertj.core.groups.Tuple;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static com.piotrglazar.lookup.TranslationDirection.ENGLISH_TO_POLISH;
import static com.piotrglazar.lookup.TranslationDirection.POLISH_TO_ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;

public class IndexUpdaterContextTest extends AbstractContextTest {

    private static final String SEPARATOR = ";";

    @Autowired
    private IndexUpdater indexUpdater;

    @Autowired
    private Searcher searcher;

    @Test
    public void shouldAddNewContentToIndex() {
        // given
        final List<String> rawDocuments = Lists.newArrayList("parrot ; papuga");

        // when
        indexUpdater.updateIndex(rawDocuments, SEPARATOR, ENGLISH_TO_POLISH);

        // then
        final Set<SearchResults> dogSearch = searcher.searchAll("parrot");
        assertThat(dogSearch).hasSize(1).extracting("english", "polish").contains(new Tuple("parrot", "papuga"));
    }

    @Test
    public void shouldNotAddNewContentIfItIsAlreadyInIndex() {
        // given
        assertThatEnglishSearchContainsExactlyNResults("macula", 3);
        final List<String> rawDocuments = Lists.newArrayList("macula ; plamka");

        // when
        indexUpdater.updateIndex(rawDocuments, SEPARATOR, ENGLISH_TO_POLISH);

        // then
        assertThatEnglishSearchContainsExactlyNResults("macula", 3);
    }

    @Test
    public void shouldAddNewMeaningForWordAndAcceptHomonyms() {
        // given
        final List<String> rawDocuments = Lists.newArrayList("zamek ; castle", "zamek ; lock");

        // when
        indexUpdater.updateIndex(rawDocuments, SEPARATOR, POLISH_TO_ENGLISH);

        // then
        assertThatPolishSearchContainsExactlyNResults("zamek", 2);
    }

    private void assertThatPolishSearchContainsExactlyNResults(String query, int expectedSize) {
        assertThat(searcher.searchInPolish(query)).hasSize(expectedSize);
    }

    private void assertThatEnglishSearchContainsExactlyNResults(String query, int expectedSize) {
        assertThat(searcher.searchInEnglish(query)).hasSize(expectedSize);
    }
}
