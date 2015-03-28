package com.piotrglazar.lookup.controllers;

import com.piotrglazar.lookup.controllers.forms.DeleteContentForm;
import com.piotrglazar.lookup.engine.Deleter;
import com.piotrglazar.lookup.domain.SearchResults;
import com.piotrglazar.lookup.engine.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class DeleteContentController {

    private final Searcher searcher;
    private final Deleter deleter;

    @Autowired
    public DeleteContentController(Searcher searcher, Deleter deleter) {
        this.searcher = searcher;
        this.deleter = deleter;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/deleteContent")
    public String showDeletePage(@ModelAttribute DeleteContentForm deleteContentForm, Model model) {
        List<SearchResults> resultsToDelete =
                searcher.searchExact(deleteContentForm.getEnglish(), deleteContentForm.getPolish());
        model.addAttribute("resultsToDelete", resultsToDelete);
        model.addAttribute("english", deleteContentForm.getEnglish());
        model.addAttribute("polish", deleteContentForm.getPolish());
        return "deleteContent";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/doDeleteContent")
    public String doDeleteContent(@ModelAttribute DeleteContentForm deleteContentForm) {
        deleter.deleteContent(deleteContentForm.getEnglish(), deleteContentForm.getPolish());
        return "redirect:/search";
    }
}
