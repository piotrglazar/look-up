package com.piotrglazar.lookup.engine;

import org.apache.lucene.search.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Deleter {

    private final LookUpIndex lookUpIndex;
    private final QueryBuilder queryBuilder;

    @Autowired
    public Deleter(LookUpIndex lookUpIndex, QueryBuilder queryBuilder) {
        this.lookUpIndex = lookUpIndex;
        this.queryBuilder = queryBuilder;
    }

    public void deleteContent(String english, String polish) {
        Query exactQuery = queryBuilder.buildExactQuery(english, polish);
        lookUpIndex.delete(exactQuery);
    }
}
