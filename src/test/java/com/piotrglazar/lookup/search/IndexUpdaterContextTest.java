package com.piotrglazar.lookup.search;

import com.google.common.collect.Lists;
import com.piotrglazar.lookup.AbstractContextTest;
import org.assertj.core.groups.Tuple;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

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
        final List<String> rawDocuments = Lists.newArrayList("dog ; pies");

        // when
        indexUpdater.updateIndex(rawDocuments, SEPARATOR);

        // then
        final Set<SearchResults> dogSearch = searcher.searchAll("dog");
        assertThat(dogSearch).hasSize(1).extracting("english", "polish").contains(new Tuple("dog", "pies"));
    }

    @Test
    public void shouldNotAddNewContentIfItIsAlreadyInIndex() {
        // given
        assertThatEnglishSearchContainsExactlyNResults("macula", 3);
        final List<String> rawDocuments = Lists.newArrayList("macula ; plamka");

        // when
        indexUpdater.updateIndex(rawDocuments, SEPARATOR);

        // then
        assertThatEnglishSearchContainsExactlyNResults("macula", 3);
    }

    @Test
    public void shouldAddNewMeaningForWordAndAcceptHomonyms() {
        // given
        final List<String> rawDocuments = Lists.newArrayList("castle ; zamek", "lock ; zamek");

        // when
        indexUpdater.updateIndex(rawDocuments, SEPARATOR);

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
