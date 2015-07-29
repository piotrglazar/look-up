package com.piotrglazar.lookup.configuration;

import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FilePathResolver {

    private final String indexLocation;
    private final String sourceLocation;

    @Autowired
    public FilePathResolver(@Value("#{configProperties['index.location']}") String indexLocation,
                            @Value("#{configProperties['source.location']}") String sourceLocation) {
        this.indexLocation = indexLocation;
        this.sourceLocation = sourceLocation;
    }

    private String getIndexDirectory() {
        return System.getProperty("user.dir") + "/" + indexLocation;
    }

    public Path getIndexPath() {
        return Paths.get(getIndexDirectory());
    }

    public URI getSourceFile() {
        try {
            return new ClassPathResource(sourceLocation).getURI();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to fetch source file location", e);
        }
    }

    public Path getSourceFilePath() {
        return Paths.get(getSourceFile());
    }

    public FSDirectory getIndexFsDirectory() {
        try {
            return FSDirectory.open(new File(getIndexDirectory()).toPath());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to open index directory for writing", e);
        }
    }
}
