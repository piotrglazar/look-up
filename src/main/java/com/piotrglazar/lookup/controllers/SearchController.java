package com.piotrglazar.lookup.controllers;

import com.google.common.collect.ImmutableMap;
import com.piotrglazar.lookup.domain.SearchResults;
import com.piotrglazar.lookup.engine.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

@Controller
public class SearchController {

    private final Searcher searcher;

    @Autowired
    public SearchController(Searcher searcher) {
        this.searcher = searcher;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public String getSearchPage(SearchForm searchForm) {
        return "search";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/search")
    public ModelAndView performSearch(SearchForm searchForm) {
        final Set<SearchResults> results = searcher.searchAll(searchForm.getQuery());
        return new ModelAndView("search", ImmutableMap.of("searchResults", results));
    }
}
