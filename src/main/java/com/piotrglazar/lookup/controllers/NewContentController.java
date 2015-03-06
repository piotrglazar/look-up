package com.piotrglazar.lookup.controllers;

import com.piotrglazar.lookup.search.IndexUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class NewContentController {

    private final IndexUpdater indexUpdater;

    @Autowired
    public NewContentController(IndexUpdater indexUpdater) {
        this.indexUpdater = indexUpdater;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/newContent")
    public String newContent(NewContentForm newContentForm) {
        return "newContent";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/newContent")
    public String insertNewContent(NewContentForm newContentForm) {
        indexUpdater.updateIndex(newContentForm.getNewContentAsLines(), newContentForm.getSeparator());
        return "redirect:/search";
    }
}
