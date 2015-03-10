package com.piotrglazar.lookup.controllers;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.piotrglazar.lookup.AbstractContextTest;
import com.piotrglazar.lookup.controllers.forms.NewContentForm;
import com.piotrglazar.lookup.search.Searcher;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static com.piotrglazar.lookup.TranslationDirection.ENGLISH_TO_POLISH;
import static com.piotrglazar.lookup.TranslationDirection.POLISH_TO_ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NewContentControllerContextTest extends AbstractContextTest {

    @Autowired
    private Searcher searcher;

    @Test
    public void shouldAccessNewContentPage() throws Exception {
        // expect
        mockMvc.perform(get("/newContent"))
            .andExpect(status().isOk());
    }

    @Test
    public void shouldAddNewContent() throws Exception {
        // when
        mockMvc.perform(post("/newContent")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("separator", NewContentForm.DEFAULT_SEPARATOR)
            .param("translationDirection", ENGLISH_TO_POLISH.toString())
            .param("newContent", Joiner.on("\n").join(Lists.newArrayList("computer ; komputer", "nprocessor ; procesor"))))

        // then
            .andExpect(status().isFound());
        assertThat(searcher.searchAll("computer")).hasSize(1);
        assertThat(searcher.searchAll("procesor")).hasSize(1);
    }

    @Test
    public void shouldNotAddNewContentIfItIsAlreadyInIndex() throws Exception {
        // when
        mockMvc.perform(post("/newContent")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("separator", NewContentForm.DEFAULT_SEPARATOR)
            .param("translationDirection", POLISH_TO_ENGLISH.toString())
            .param("newContent", "cynk ; zinc"))

        // then
            .andExpect(status().isFound());
        assertThat(searcher.searchAll("zinc")).hasSize(1);
    }
}
