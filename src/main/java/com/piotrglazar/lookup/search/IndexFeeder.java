package com.piotrglazar.lookup.search;

import com.google.common.base.Charsets;
import com.piotrglazar.lookup.configuration.FilePathResolver;
import org.apache.lucene.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class IndexFeeder {

    private final FilePathResolver filePathResolver;
    private final DocumentFactory documentFactory;

    @Autowired
    public IndexFeeder(FilePathResolver filePathResolver, DocumentFactory documentFactory) {
        this.filePathResolver = filePathResolver;
        this.documentFactory = documentFactory;
    }

    public List<Document> feed() {
        return feed(getLinesFromSourceFile(), ";");
    }

    public List<Document> feed(List<String> source, String separator) {
        return source.stream()
                .filter(line -> !line.isEmpty())
                .filter(line -> line.contains(separator))
                .map(line -> line.split(separator))
                .map(this::buildDocument)
                .collect(toList());
    }

    private Document buildDocument(String[] tokens) {
        return documentFactory.createDocument(tokens[0].trim(), tokens[1].trim());
    }

    private List<String> getLinesFromSourceFile() {
        try {
            return Files.readAllLines(filePathResolver.getSourceFilePath(), Charsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to get lines from source file " + filePathResolver.getSourceFile(), e);
        }
    }
}
