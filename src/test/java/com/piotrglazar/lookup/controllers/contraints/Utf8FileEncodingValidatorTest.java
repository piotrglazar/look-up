package com.piotrglazar.lookup.controllers.contraints;

import com.piotrglazar.lookup.controllers.forms.NewContentFileForm;
import com.piotrglazar.lookup.utils.EncodingDetector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class Utf8FileEncodingValidatorTest {

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private EncodingDetector encodingDetector;

    @InjectMocks
    private Utf8FileEncodingValidator validator;

    @Test
    public void shouldMarkFormAsInvalidWhenExceptionOccurs() throws IOException {
        // given
        given(multipartFile.getBytes()).willThrow(new IOException("test exception"));
        final NewContentFileForm form = formWithFile();

        // when
        final boolean valid = validator.isValid(form, context);

        // then
        assertThat(valid).isFalse();
    }

    @Test
    public void shouldNotPerformValidationWhenOverrideIsSelected() {
        // given
        final NewContentFileForm form = formWithFileAndOverride(true);

        // when
        final boolean valid = validator.isValid(form, context);

        // then
        assertThat(valid).isTrue();
    }

    @Test
    public void shouldUseEncodingDetector() {
        // given
        given(encodingDetector.hasProbablyUtf8Encoding(any(byte[].class))).willReturn(true);
        final NewContentFileForm form = formWithFile();

        // when
        final boolean valid = validator.isValid(form, context);

        // then
        assertThat(valid).isTrue();
    }

    private NewContentFileForm formWithFile() {
        return formWithFileAndOverride(false);
    }

    private NewContentFileForm formWithFileAndOverride(boolean override) {
        NewContentFileForm form = new NewContentFileForm();
        form.setNewContent(multipartFile);
        form.setOverride(override);
        return form;
    }
}
