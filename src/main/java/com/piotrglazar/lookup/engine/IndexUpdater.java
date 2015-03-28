package com.piotrglazar.lookup.engine;

import com.piotrglazar.lookup.TranslationDirection;
import com.piotrglazar.lookup.domain.LookUpDocument;
import com.piotrglazar.lookup.domain.SearchResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;

@Component
public class IndexUpdater {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Searcher searcher;
    private final LookUpIndex lookUpIndex;
    private final IndexFeeder indexFeeder;

    @Autowired
    public IndexUpdater(Searcher searcher, LookUpIndex lookUpIndex, IndexFeeder indexFeeder) {
        this.searcher = searcher;
        this.lookUpIndex = lookUpIndex;
        this.indexFeeder = indexFeeder;
    }

    public void updateIndex(List<String> newContent, String separator, TranslationDirection translationDirection) {
        final List<LookUpDocument> documents = indexFeeder.feed(newContent, separator, translationDirection);

        final Map<Boolean, List<LookUpDocument>> groupedDocuments = documents.stream()
                .collect(groupingBy(this::isNewDocument));

        logDuplicatedEntries(groupedDocuments.getOrDefault(Boolean.FALSE, Collections.emptyList()));
        saveNewEntries(groupedDocuments.getOrDefault(Boolean.TRUE, Collections.emptyList()));
    }

    private void saveNewEntries(final List<LookUpDocument> documents) {
        lookUpIndex.addToIndex(documents);
    }

    private void logDuplicatedEntries(final List<LookUpDocument> documents) {
        documents.stream().forEach(d -> LOG.info("{} - {} entry is already in index", d.getEnglish(), d.getPolish()));
    }

    private boolean isNewDocument(LookUpDocument document) {
        String english = document.getEnglish();
        String polish = document.getPolish();
        return hasNoResultsMatchingExactly(english, searcher.searchInEnglish(english), SearchResults::getEnglish)
                && hasNoResultsMatchingExactly(polish, searcher.searchInPolish(polish), SearchResults::getPolish);
    }

    private boolean hasNoResultsMatchingExactly(String source, List<SearchResults> searchResults,
                                                Function<SearchResults, String> mapper) {
        return searchResults.stream().map(mapper).filter(source::equals).count() == 0;
    }
}
