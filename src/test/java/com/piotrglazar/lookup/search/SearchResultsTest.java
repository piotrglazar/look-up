package com.piotrglazar.lookup.search;

import com.google.common.collect.Lists;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchResultsTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(SearchResults.class).verify();
    }

    @Test
    public void shouldCompareByScore() {
        // given
        final SearchResults first = new SearchResults(1, "en", "pl", 2.0f);
        final SearchResults second = new SearchResults(2, "en", "pl", 16.0f);
        final SearchResults third = new SearchResults(3, "en", "pl", 8.0f);
        final SearchResults fourth = new SearchResults(4, "en", "pl", 4.0f);

        // when
        final ArrayList<SearchResults> searchResults = Lists.newArrayList(first, second, third, fourth);
        Collections.sort(searchResults);

        // then
        assertThat(searchResults).containsExactly(first, fourth, third, second);
    }
}
