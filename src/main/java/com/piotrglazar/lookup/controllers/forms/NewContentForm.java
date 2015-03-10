package com.piotrglazar.lookup.controllers.forms;

import com.google.common.collect.Lists;
import com.piotrglazar.lookup.TranslationDirection;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class NewContentForm {

    public static final String DEFAULT_SEPARATOR = ";";

    @NotEmpty
    private String separator = DEFAULT_SEPARATOR;

    @NotEmpty
    private String newContent;

    @NotNull
    private TranslationDirection translationDirection;

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

    public TranslationDirection getTranslationDirection() {
        return translationDirection;
    }

    public void setTranslationDirection(final TranslationDirection translationDirection) {
        this.translationDirection = translationDirection;
    }

    public List<String> getNewContentAsLines() {
        return Lists.newArrayList(newContent.split("\n"));
    }
}
