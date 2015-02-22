package com.piotrglazar.lookup.search;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class SearchResults {

    private final int index;
    private final String english;
    private final String polish;
    private final float score;

    public SearchResults(int index, String english, String polish, float score) {
        this.index = index;
        this.english = english;
        this.polish = polish;
        this.score = score;
    }

    public SearchResults(int index, DocumentWithScore document) {
        this(index, document.getEnglish(), document.getPolish(), document.getScore());
    }

    public int getIndex() {
        return index;
    }

    public String getEnglish() {
        return english;
    }

    public String getPolish() {
        return polish;
    }

    public float getScore() {
        return score;
    }
}
