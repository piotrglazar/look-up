package com.piotrglazar.lookup.engine;

import com.piotrglazar.lookup.configuration.FilePathResolver;
import com.piotrglazar.lookup.configuration.IndexWriterConfigFactory;
import com.piotrglazar.lookup.domain.LookUpDocument;
import com.piotrglazar.lookup.exceptions.DocumentDeleteException;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class LookUpIndex {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final FilePathResolver filePathResolver;
    private final IndexWriterConfigFactory indexWriterConfigFactory;
    private final IndexFeeder indexFeeder;
    private IndexSearcher indexSearcher;

    @Autowired
    public LookUpIndex(FilePathResolver filePathResolver, IndexWriterConfigFactory indexWriterConfigFactory, IndexFeeder indexFeeder) {
        this.filePathResolver = filePathResolver;
        this.indexWriterConfigFactory = indexWriterConfigFactory;
        this.indexFeeder = indexFeeder;
    }

    @PostConstruct
    public void buildIndexFromSource() {
        if (indexIsEmpty()) {
            addToIndex(indexFeeder.feed());
        } else {
            this.indexSearcher = buildSearchIndex();
        }
    }

    public void addToIndex(List<LookUpDocument> documents) {
        final IndexWriter indexWriter = getIndexWriter();

        documents.forEach(document -> addDocumentToIndex(indexWriter, document.getDocument()));

        closeWriterAndUpdateSearcher(indexWriter);
    }

    public IndexSearcher getIndexSearcher() {
        return indexSearcher;
    }

    public void delete(Query deleteQuery) {
        IndexWriter indexWriter = getIndexWriter();
        try {
            indexWriter.deleteDocuments(deleteQuery);
        } catch (IOException e) {
            throw new DocumentDeleteException(deleteQuery.toString(), e);
        }
        closeWriterAndUpdateSearcher(indexWriter);
    }

    private void closeWriterAndUpdateSearcher(IndexWriter indexWriter) {
        closeIndex(indexWriter);
        this.indexSearcher = buildSearchIndex();
    }

    private IndexWriter getIndexWriter() {
        try {
            return new IndexWriter(filePathResolver.getIndexFsDirectory(), indexWriterConfigFactory.indexWriterConfig());
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
