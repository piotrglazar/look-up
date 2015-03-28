package com.piotrglazar.lookup.controllers.forms;

import com.piotrglazar.lookup.TranslationDirection;
import com.piotrglazar.lookup.controllers.constraints.Utf8FileEncoding;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Utf8FileEncoding(message = "File encoding must be UTF-8")
public class NewContentFileForm {

    public static final String DEFAULT_SEPARATOR = ";";

    @NotEmpty
    private String separator = DEFAULT_SEPARATOR;

    @NotNull
    private MultipartFile newContent;

    @NotNull
    private TranslationDirection translationDirection;

    @NotNull
    private Boolean override;

    public Boolean getOverride() {
        return override;
    }

    public void setOverride(Boolean override) {
        this.override = override;
    }

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
