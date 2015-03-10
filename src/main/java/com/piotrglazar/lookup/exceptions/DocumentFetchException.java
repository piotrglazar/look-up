package com.piotrglazar.lookup.exceptions;

public class DocumentFetchException extends RuntimeException {

    private static final long serialVersionUID = 1;

    public DocumentFetchException(long documentId, Throwable cause) {
        super("Failed to fetch document with id " + documentId, cause);
    }
}
