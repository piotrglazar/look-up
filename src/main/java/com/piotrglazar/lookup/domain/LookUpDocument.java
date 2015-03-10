package com.piotrglazar.lookup.domain;

import org.apache.lucene.document.Document;

public class LookUpDocument {

    public static final String ENGLISH_FIELD = "english";
    public static final String POLISH_FIELD = "polish";

    private final Document document;

    public LookUpDocument(Document document) {
        this.document = document;
    }

    public String getEnglish() {
        return document.get(ENGLISH_FIELD);
    }

    public String getPolish() {
        return document.get(POLISH_FIELD);
    }

    public Document getDocument() {
        return document;
    }
}
