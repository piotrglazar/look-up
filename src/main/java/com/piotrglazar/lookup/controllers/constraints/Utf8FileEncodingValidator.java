package com.piotrglazar.lookup.controllers.constraints;

import com.piotrglazar.lookup.controllers.forms.NewContentFileForm;
import com.piotrglazar.lookup.utils.EncodingDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class Utf8FileEncodingValidator implements ConstraintValidator<Utf8FileEncoding, NewContentFileForm> {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final EncodingDetector encodingDetector;

    @Autowired
    public Utf8FileEncodingValidator(EncodingDetector encodingDetector) {
        this.encodingDetector = encodingDetector;
    }

    @Override
    public void initialize(final Utf8FileEncoding constraintAnnotation) {

    }

    @Override
    public boolean isValid(NewContentFileForm form, ConstraintValidatorContext context) {
        LOG.info("Validating");
        if (form.getOverride()) {
            return true;
        } else {
            try {
                return encodingDetector.hasProbablyUtf8Encoding(form.getNewContent().getBytes());
            } catch (IOException e) {
                LOG.error("Failed to get file content", e);
                return false;
            }
        }
    }
}
