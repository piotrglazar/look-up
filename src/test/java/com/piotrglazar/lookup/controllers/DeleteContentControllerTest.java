package com.piotrglazar.lookup.controllers;

import com.piotrglazar.lookup.AbstractContextTest;
import com.piotrglazar.lookup.engine.IndexUpdater;
import com.piotrglazar.lookup.engine.Searcher;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static com.piotrglazar.lookup.TranslationDirection.ENGLISH_TO_POLISH;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

public class DeleteContentControllerTest extends AbstractContextTest {

    @Autowired
    private Searcher searcher;

    @Autowired
    private IndexUpdater indexUpdater;

    @Test
    public void shouldPerformSpecificSearchBeforeDelete() throws Exception {
        // given
        addContent("englishDel", "polishDel1", "polishDel2");

        // when
        mockMvc.perform(post("/deleteContent")
                .param("english", "englishDel")
                .param("polish", "polishDel1"))

        // then
            .andExpect(status().isOk())
            .andExpect(xpath("//*[@class='searchResult']").nodeCount(1));

        assertThat(searcher.searchAll("englishDel")).hasSize(2);
    }

    @Test
    public void shouldDeleteContentAndRedirectTo() throws Exception {
        // given
        addContent("englishDelete", "polishDelete1", "polishDelete2");

        // when
        mockMvc.perform(post("/doDeleteContent")
                .param("english", "englishDelete")
                .param("polish", "polishDelete1"))

        // then
            .andExpect(status().isFound());

        assertThat(searcher.searchAll("englishDelete")).hasSize(1);
    }

    private void addContent(String english, String... polish) {
        List<String> content = Arrays.stream(polish).map(p -> english + ";" + p).collect(toList());
        indexUpdater.updateIndex(content, ";", ENGLISH_TO_POLISH);
    }
}
