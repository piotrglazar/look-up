package com.piotrglazar.lookup.search;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

public class SearchException extends RuntimeException {

    private static final long serialVersionUID = 1;

    public SearchException(String keyField, String rawQuery, ParseException e) {
        super(String.format("Failed to create query %s for %s", rawQuery, keyField), e);
    }

    public SearchException(IOException e) {
        super("Index search failed", e);
    }
}
