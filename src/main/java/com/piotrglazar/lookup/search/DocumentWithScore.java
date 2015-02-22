package com.piotrglazar.lookup.search;

import org.apache.lucene.document.Document;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class DocumentWithScore {
    private final Document document;
    private final float score;

    DocumentWithScore(Document document, float score) {
        this.document = document;
        this.score = score;
    }

    public String getEnglish() {
        return document.get("english");
    }

    public String getPolish() {
        return document.get("polish");
    }

    public float getScore() {
        return score;
    }
}
