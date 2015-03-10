package com.piotrglazar.lookup.controllers;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IndexControllerTest {

    @Test
    public void shouldRedirectToSearchPage() {
        // given
        final IndexController indexController = new IndexController();

        // when
        final String redirect = indexController.redirectFromIndex();

        // then
        assertThat(redirect).isEqualTo("redirect:/search");
    }
}
