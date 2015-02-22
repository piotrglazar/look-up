package com.piotrglazar.lookup.search;

import com.piotrglazar.lookup.configuration.FilePathResolver;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class LookUpIndex {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final FilePathResolver filePathResolver;
    private final IndexWriterConfig indexWriterConfig;
    private final IndexFeeder indexFeeder;
    private IndexSearcher indexSearcher;

    @Autowired
    public LookUpIndex(FilePathResolver filePathResolver, IndexWriterConfig indexWriterConfig, IndexFeeder indexFeeder) {
        this.filePathResolver = filePathResolver;
        this.indexWriterConfig = indexWriterConfig;
        this.indexFeeder = indexFeeder;
    }

    @PostConstruct
    public void buildIndexFromSource() {
        if (indexIsEmpty()) {
            final IndexWriter indexWriter = getIndexWriter(filePathResolver, indexWriterConfig);

            indexFeeder.feed().forEach(document -> addDocumentToIndex(indexWriter, document));

            closeIndex(indexWriter);
        }
        this.indexSearcher = buildSearchIndex();
    }

    private IndexWriter getIndexWriter(FilePathResolver filePathResolver, IndexWriterConfig indexWriterConfig) {
        try {
            return new IndexWriter(filePathResolver.getIndexFsDirectory(), indexWriterConfig);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to open index writer", e);
        }
    }

    private IndexSearcher buildSearchIndex() {
        try {
            IndexReader reader = DirectoryReader.open(filePathResolver.getIndexFsDirectory());
            return new IndexSearcher(reader);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create index reader", e);
        }
    }

    public IndexSearcher getIndexSearcher() {
        return indexSearcher;
    }

    private void addDocumentToIndex(final IndexWriter writer, final Document document) {
        try {
            writer.addDocument(document);
        } catch (IOException e) {
            LOG.error("Failed to add document to index", e);
        }
    }

    private boolean indexIsEmpty() {
        try {
            // 1 because write.lock file is always created
            final Path indexPath = filePathResolver.getIndexPath();
            return !indexPath.toFile().exists() || Files.list(indexPath).count() <= 1;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to check whether index directory is empty or not", e);
        }
    }

    private void closeIndex(final IndexWriter writer) {
        try {
            writer.close();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write index", e);
        }
    }
}
