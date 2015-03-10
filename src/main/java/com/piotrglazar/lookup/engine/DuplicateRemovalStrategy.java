package com.piotrglazar.lookup.engine;

import com.piotrglazar.lookup.domain.LookUpDocument;

import java.util.List;

@FunctionalInterface
public interface DuplicateRemovalStrategy {

    List<LookUpDocument> removeDuplicates(List<LookUpDocument> documents);
}
