package com.piotrglazar.lookup.controllers;

import com.piotrglazar.lookup.AbstractContextTest;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

public class SearchControllerContextTest extends AbstractContextTest {

    @Test
    public void shouldAccessSearchPage() throws Exception {
        // expect
        mockMvc.perform(get("/search"))
            .andExpect(status().isOk());
    }

    @Test
    public void shouldPerformSearch() throws Exception {
        // given
        final String query = "win*";

        // when
        mockMvc.perform(post("/search")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("query", query))
        // then
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("searchResults"))
            .andExpect(xpath("//tr[@class='searchResult']").nodeCount(2));
    }
}
