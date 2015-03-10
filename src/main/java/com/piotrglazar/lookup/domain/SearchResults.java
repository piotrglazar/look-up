package com.piotrglazar.lookup.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class SearchResults implements Comparable<SearchResults> {

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

    public SearchResults(int index, LookUpDocument document, float score) {
        this(index, document.getEnglish(), document.getPolish(), score);
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

    @Override
    public int hashCode() {
        return Objects.hashCode(index, english, polish, score);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SearchResults other = (SearchResults) obj;
        return Objects.equal(this.index, other.index)
                && Objects.equal(this.english, other.english)
                && Objects.equal(this.polish, other.polish)
                && Objects.equal(this.score, other.score);
    }

    @Override
    public int compareTo(SearchResults that) {
        return Float.compare(this.score, that.score);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("index", index)
                .add("english", english)
                .add("polish", polish)
                .add("score", score)
                .toString();
    }
}
