package com.piotrglazar.lookup.controllers;

import com.piotrglazar.lookup.TranslationDirection;
import com.piotrglazar.lookup.controllers.forms.NewContentFileForm;
import com.piotrglazar.lookup.engine.IndexUpdater;
import com.piotrglazar.lookup.utils.ByteArrayToStringListConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class NewContentFileController {

    private final ByteArrayToStringListConverter converter;
    private final IndexUpdater indexUpdater;

    @Autowired
    public NewContentFileController(ByteArrayToStringListConverter converter, IndexUpdater indexUpdater) {
        this.converter = converter;
        this.indexUpdater = indexUpdater;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/newContentFile")
    public String newContent(NewContentFileForm newContentFileForm, Model model) {
        model.addAttribute("translationDirections", TranslationDirection.values());
        return "newContentFile";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/newContentFile")
    public String insertNewContent(@Valid NewContentFileForm newContentFileForm, BindingResult bindingResult, Model model)
            throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("translationDirections", TranslationDirection.values());
            return "newContentFile";
        }
        final byte[] bytes = newContentFileForm.getNewContent().getBytes();
        final List<String> content = converter.convert(bytes);
        indexUpdater.updateIndex(content, newContentFileForm.getSeparator(), newContentFileForm.getTranslationDirection());
        return "redirect:/search";
    }
}
