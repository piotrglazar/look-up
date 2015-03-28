package com.piotrglazar.lookup.exceptions;

public class DocumentDeleteException extends RuntimeException {

    public DocumentDeleteException(String query, Throwable cause) {
        super(query, cause);
    }
}
