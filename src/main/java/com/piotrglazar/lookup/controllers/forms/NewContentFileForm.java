package com.piotrglazar.lookup.controllers.forms;

import com.piotrglazar.lookup.TranslationDirection;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class NewContentFileForm {

    public static final String DEFAULT_SEPARATOR = ";";

    @NotEmpty
    private String separator = DEFAULT_SEPARATOR;

    @NotNull
    private MultipartFile newContent;

    @NotNull
    private TranslationDirection translationDirection;

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public MultipartFile getNewContent() {
        return newContent;
    }

    public void setNewContent(MultipartFile newContent) {
        this.newContent = newContent;
    }

    public TranslationDirection getTranslationDirection() {
        return translationDirection;
    }

    public void setTranslationDirection(TranslationDirection translationDirection) {
        this.translationDirection = translationDirection;
    }
}
