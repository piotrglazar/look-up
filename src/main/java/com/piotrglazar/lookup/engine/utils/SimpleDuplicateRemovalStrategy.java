package com.piotrglazar.lookup.engine.utils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.piotrglazar.lookup.domain.LookUpDocument;
import com.piotrglazar.lookup.engine.DuplicateRemovalStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SimpleDuplicateRemovalStrategy implements DuplicateRemovalStrategy {

    @Override
    public List<LookUpDocument> removeDuplicates(List<LookUpDocument> documents) {
        // remove duplicates by checking whether or not documents are equal (ignoring letter case)
        Map<String, LookUpDocument> docs = Maps.newHashMap();
        documents.stream().forEach(d -> docs.putIfAbsent(joinEnglishAndPolishPartsLowercase(d), d));
        return Lists.newLinkedList(docs.values());
    }

    private String joinEnglishAndPolishPartsLowercase(LookUpDocument document) {
        return Joiner.on("|").join(document.getEnglish().toLowerCase(), document.getPolish().toLowerCase());
    }
}
