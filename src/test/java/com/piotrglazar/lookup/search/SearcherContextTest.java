package com.piotrglazar.lookup.search;

import com.google.common.collect.Lists;
import com.piotrglazar.lookup.AbstractContextTest;
import junitparams.Parameters;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SearcherContextTest extends AbstractContextTest {

    @Autowired
    private Searcher searcher;

    @Test
    @Parameters({
            "acrylic | acrylic matrix - macierz akrylowa & acrylic polymer - polimer akrylowy",
            "hypo* | hypodermis - tkanka podskórna & hypokalemia - hiperkaliemia & hyponatremia - hiponatremia"
    })
    public void shouldSearchGivenTerms(String searchTerm, String rawResults) throws IOException, ParseException {
        // given
        final ExpectedSearchResults expectedSearchResults = ExpectedSearchResults.fromRawResults(rawResults);

        // when
        final List<SearchResults> searchResults = searcher.searchInEnglish(searchTerm);

        // then
        assertThat(searchResults).hasSize(expectedSearchResults.getSize());
        for (int i = 0; i < expectedSearchResults.getSize(); ++i) {
            assertThatSearchResultContains(searchResults.get(i), expectedSearchResults.getEnglish(i), expectedSearchResults.getPolish(i));
        }
    }

    private void assertThatSearchResultContains(SearchResults searchResults, String expectedEnglish, String expectedPolish) {
        assertThat(searchResults.getEnglish()).isEqualTo(expectedEnglish);
        assertThat(searchResults.getPolish()).isEqualTo(expectedPolish);
    }

    private static final class ExpectedSearchResults {

        private final List<String> english;
        private final List<String> polish;
        private final int size;

        private ExpectedSearchResults(List<String> english, List<String> polish, int size) {
            this.english = english;
            this.polish = polish;
            this.size = size;
        }

        public String getEnglish(int i) {
            return english.get(i);
        }

        public String getPolish(int i) {
            return polish.get(i);
        }

        public int getSize() {
            return size;
        }

        public static ExpectedSearchResults fromRawResults(String rawResults) {
            List<String> english = Lists.newLinkedList();
            List<String> polish = Lists.newLinkedList();

            Arrays.stream(rawResults.split("&")).forEach(engPol -> {
                final String[] tokens = engPol.split("-");
                english.add(tokens[0].trim());
                polish.add(tokens[1].trim());
            });

            assertThat(english.size()).isEqualTo(polish.size());
            return new ExpectedSearchResults(english, polish, english.size());
        }
    }
}
