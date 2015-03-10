package com.piotrglazar.lookup.controllers;

import com.piotrglazar.lookup.TranslationDirection;
import com.piotrglazar.lookup.controllers.forms.NewContentForm;
import com.piotrglazar.lookup.engine.IndexUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class NewContentController {

    private final IndexUpdater indexUpdater;

    @Autowired
    public NewContentController(IndexUpdater indexUpdater) {
        this.indexUpdater = indexUpdater;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/newContent")
    public String newContent(NewContentForm newContentForm, Model model) {
        model.addAttribute("translationDirections", TranslationDirection.values());
        return "newContent";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/newContent")
    public String insertNewContent(@Valid NewContentForm newContentForm) {
        indexUpdater.updateIndex(newContentForm.getNewContentAsLines(), newContentForm.getSeparator(),
                newContentForm.getTranslationDirection());
        return "redirect:/search";
    }
}
