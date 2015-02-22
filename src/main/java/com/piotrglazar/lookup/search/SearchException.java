package com.piotrglazar.lookup.search;

public class SearchException extends RuntimeException {

    private static final long serialVersionUID = 1;

    public SearchException(long documentId, Throwable cause) {
        super("Failed to fetch document with id " + documentId, cause);
    }
}
