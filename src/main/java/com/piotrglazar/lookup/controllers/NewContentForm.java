package com.piotrglazar.lookup.controllers;

import com.google.common.collect.Lists;

import java.util.List;

public class NewContentForm {

    public static final String DEFAULT_SEPARATOR = ";";

    private String separator = DEFAULT_SEPARATOR;
    private String newContent;

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

    public List<String> getNewContentAsLines() {
        return Lists.newArrayList(newContent.split("\n"));
    }
}
