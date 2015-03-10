package com.piotrglazar.lookup.engine;

import com.google.common.base.Charsets;
import com.piotrglazar.lookup.TranslationDirection;
import com.piotrglazar.lookup.configuration.FilePathResolver;
import com.piotrglazar.lookup.domain.DocumentFactory;
import com.piotrglazar.lookup.domain.LookUpDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static com.piotrglazar.lookup.TranslationDirection.ENGLISH_TO_POLISH;
import static com.piotrglazar.lookup.TranslationDirection.POLISH_TO_ENGLISH;
import static java.util.stream.Collectors.toList;

@Component
public class IndexFeeder {

    private final FilePathResolver filePathResolver;
    private final DocumentFactory documentFactory;
    private final DuplicateRemovalStrategy strategy;

    @Autowired
    public IndexFeeder(FilePathResolver filePathResolver, DocumentFactory documentFactory, DuplicateRemovalStrategy strategy) {
        this.filePathResolver = filePathResolver;
        this.documentFactory = documentFactory;
        this.strategy = strategy;
    }

    public List<LookUpDocument> feed() {
        return feed(getLinesFromSourceFile(), ";", ENGLISH_TO_POLISH);
    }

    public List<LookUpDocument> feed(List<String> source, String separator, TranslationDirection translationDirection) {
        final List<LookUpDocument> documents = source.stream()
                .filter(line -> !line.isEmpty())
                .filter(line -> line.contains(separator))
                .map(line -> line.split(separator))
                .map(tokens -> buildDocument(tokens, translationDirection))
                .collect(toList());
        return strategy.removeDuplicates(documents);
    }

    private LookUpDocument buildDocument(String[] tokens, TranslationDirection translationDirection) {
        if (translationDirection == ENGLISH_TO_POLISH) {
            return documentFactory.createDocument(tokens[0].trim(), tokens[1].trim());
        } else if (translationDirection == POLISH_TO_ENGLISH) {
            return documentFactory.createDocument(tokens[1].trim(), tokens[0].trim());
        } else {
            throw new IllegalArgumentException("Unsupported TranslationDirection " + translationDirection);
        }
    }

    private List<String> getLinesFromSourceFile() {
        try {
            return Files.readAllLines(filePathResolver.getSourceFilePath(), Charsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to get lines from source file " + filePathResolver.getSourceFile(), e);
        }
    }
}
