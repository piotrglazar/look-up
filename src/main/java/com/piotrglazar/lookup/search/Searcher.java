package com.piotrglazar.lookup.search;

import com.piotrglazar.lookup.domain.LookUpDocument;
import com.piotrglazar.lookup.domain.SearchResults;
import com.piotrglazar.lookup.exceptions.DocumentFetchException;
import com.piotrglazar.lookup.exceptions.SearchException;
import com.piotrglazar.lookup.engine.LookUpIndex;
import com.piotrglazar.lookup.utils.SequenceGenerator;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.piotrglazar.lookup.domain.LookUpDocument.ENGLISH_FIELD;
import static com.piotrglazar.lookup.domain.LookUpDocument.POLISH_FIELD;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Component
public class Searcher {

    private final LookUpIndex lookUpIndex;
    private final StandardAnalyzer analyzer;

    @Autowired
    public Searcher(LookUpIndex lookUpIndex, StandardAnalyzer analyzer) {
        this.analyzer = analyzer;
        this.lookUpIndex = lookUpIndex;
    }

    public List<SearchResults> searchInEnglish(String rawQuery) {
        final Query query = buildQuery(ENGLISH_FIELD, rawQuery);
        return search(query);
    }

    public List<SearchResults> searchInPolish(String rawQuery) {
        final Query query = buildQuery(POLISH_FIELD, rawQuery);
        return search(query);
    }

    public Set<SearchResults> searchAll(String rawQuery) {
        final List<SearchResults> english = searchInEnglish(rawQuery);
        final List<SearchResults> polish = searchInPolish(rawQuery);
        return Stream.concat(english.stream(), polish.stream()).collect(toSet());
    }

    private Query buildQuery(String keyField, String rawQuery) {
        try {
            return new QueryParser(keyField, analyzer).parse(rawQuery);
        } catch (ParseException e) {
            throw new SearchException(keyField, rawQuery, e);
        }
    }

    private List<SearchResults> search(Query query) {
        final IndexSearcher indexSearcher = lookUpIndex.getIndexSearcher();
        final TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);

        performSearch(query, indexSearcher, collector);
        final SequenceGenerator sequenceGenerator = new SequenceGenerator();
        return Arrays.stream(collector.topDocs().scoreDocs)
                .map(scoreDoc -> new SearchResults(sequenceGenerator.next(), getDocumentWithScore(indexSearcher, scoreDoc), scoreDoc.score))
                .collect(toList());
    }

    private void performSearch(Query query, IndexSearcher indexSearcher, TopScoreDocCollector collector) {
        try {
            indexSearcher.search(query, collector);
        } catch (IOException e) {
            throw new SearchException(e);
        }
    }

    private LookUpDocument getDocumentWithScore(IndexSearcher indexSearcher, ScoreDoc scoreDoc) {
        try {
            return new LookUpDocument(indexSearcher.doc(scoreDoc.doc));
        } catch (IOException e) {
            throw new DocumentFetchException(scoreDoc.doc, e);
        }
    }

}
