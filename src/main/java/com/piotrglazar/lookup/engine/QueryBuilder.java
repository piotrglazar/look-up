package com.piotrglazar.lookup.engine;

import com.piotrglazar.lookup.exceptions.SearchException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.piotrglazar.lookup.domain.LookUpDocument.ENGLISH_FIELD;
import static com.piotrglazar.lookup.domain.LookUpDocument.POLISH_FIELD;
import static java.lang.String.format;
import static org.apache.lucene.search.BooleanClause.Occur.MUST;

@Component
public class QueryBuilder {

    private final StandardAnalyzer analyzer;

    @Autowired
    public QueryBuilder(StandardAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public Query buildQuery(String keyField, String rawQuery) {
        try {
            return new QueryParser(keyField, analyzer).parse(rawQuery);
        } catch (ParseException e) {
            throw new SearchException(keyField, rawQuery, e);
        }
    }

    public Query buildExactQuery(String rawEnglishQuery, String rawPolishQuery) {
        String queryTemplate = "%s:\"%s\"";
        Query englishQuery = buildQuery(ENGLISH_FIELD, format(queryTemplate, ENGLISH_FIELD, rawEnglishQuery));
        Query polishQuery = buildQuery(POLISH_FIELD, format(queryTemplate, POLISH_FIELD, rawPolishQuery));
        BooleanQuery exactQuery = new BooleanQuery();
        exactQuery.add(englishQuery, MUST);
        exactQuery.add(polishQuery, MUST);
        return exactQuery;
    }
}
