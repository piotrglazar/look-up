package com.piotrglazar.lookup.engine.utils;

import com.google.common.collect.Lists;
import com.piotrglazar.lookup.domain.DocumentFactory;
import com.piotrglazar.lookup.domain.LookUpDocument;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleDuplicateRemovalStrategyTest {

    private DocumentFactory documentFactory = new DocumentFactory();
    private SimpleDuplicateRemovalStrategy strategy = new SimpleDuplicateRemovalStrategy();

    @Test
    public void shouldNotCrashWhenNoDocumentsProvided() {
        // when
        final List<LookUpDocument> documents = strategy.removeDuplicates(Collections.<LookUpDocument>emptyList());

        // then
        assertThat(documents).isEmpty();
    }

    @Test
    public void shouldRemoveDuplicatesWhichDifferByLetterCase() {
        // given
        final ArrayList<LookUpDocument> documents =
                Lists.newArrayList(documentFactory.createDocument("cat", "kot"), documentFactory.createDocument("cAt", "kOT"));

        // when
        final List<LookUpDocument> noDuplicates = strategy.removeDuplicates(documents);

        // then
        assertThat(noDuplicates)
                .hasSize(1)
                .containsExactly(documents.get(0));
    }
}
