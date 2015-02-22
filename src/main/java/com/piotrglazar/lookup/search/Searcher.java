package com.piotrglazar.lookup.search;

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

import static java.util.stream.Collectors.toList;

@Component
public class Searcher {

    private final LookUpIndex lookUpIndex;
    private final StandardAnalyzer analyzer;

    @Autowired
    public Searcher(LookUpIndex lookUpIndex, StandardAnalyzer analyzer) {
        this.analyzer = analyzer;
        this.lookUpIndex = lookUpIndex;
    }

    public List<SearchResults> searchInEnglish(String rawQuery) throws ParseException, IOException {
        final IndexSearcher indexSearcher = lookUpIndex.getIndexSearcher();
        final Query query = new QueryParser("english", analyzer).parse(rawQuery);
        final TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);

        indexSearcher.search(query, collector);
        final SequenceGenerator sequenceGenerator = new SequenceGenerator();
        return Arrays.stream(collector.topDocs().scoreDocs)
            .map(scoreDoc -> getDocumentWithScore(indexSearcher, scoreDoc))
            .map(document -> new SearchResults(sequenceGenerator.next(), document))
            .collect(toList());
    }

    private DocumentWithScore getDocumentWithScore(IndexSearcher indexSearcher, ScoreDoc scoreDoc) {
        try {
            return new DocumentWithScore(indexSearcher.doc(scoreDoc.doc), scoreDoc.score);
        } catch (IOException e) {
            throw new SearchException(scoreDoc.doc, e);
        }
    }

}
