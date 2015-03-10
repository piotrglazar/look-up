package com.piotrglazar.lookup.controllers;

import com.piotrglazar.lookup.AbstractContextTest;
import com.piotrglazar.lookup.controllers.forms.NewContentForm;
import com.piotrglazar.lookup.search.Searcher;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import static com.piotrglazar.lookup.TranslationDirection.ENGLISH_TO_POLISH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NewContentFileControllerContextTest extends AbstractContextTest {

    @Autowired
    private Searcher searcher;

    @Test
    public void shouldAccessNewContentFileUploadPage() throws Exception {
        // expect
        mockMvc.perform(get("/newContentFile"))
            .andExpect(status().isOk());
    }

    @Test
    public void shouldAddContentFromUploadedFile() throws Exception {
        // given
        final MockMultipartFile upload = new MockMultipartFile("newContent", new ClassPathResource("upload/upload.txt").getInputStream());

        // when
        mockMvc.perform(fileUpload("/newContentFile")
            .file(upload)
            .param("separator", NewContentForm.DEFAULT_SEPARATOR)
            .param("translationDirection", ENGLISH_TO_POLISH.toString()))

        // then
            .andExpect(status().isFound());
        assertThat(searcher.searchAll("cat")).hasSize(1);
        assertThat(searcher.searchAll("dog")).hasSize(1);
        assertThat(searcher.searchAll("ptak")).hasSize(1);
    }
}
