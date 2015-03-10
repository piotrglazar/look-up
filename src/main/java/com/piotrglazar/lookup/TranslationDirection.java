package com.piotrglazar.lookup;

public enum TranslationDirection {

    ENGLISH_TO_POLISH("From English to Polish"),
    POLISH_TO_ENGLISH("From Polish to English");

    private String text;

    private TranslationDirection(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
