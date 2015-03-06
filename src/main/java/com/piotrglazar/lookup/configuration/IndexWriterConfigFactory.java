package com.piotrglazar.lookup.configuration;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IndexWriterConfigFactory {

    private final StandardAnalyzer standardAnalyzer;

    @Autowired
    public IndexWriterConfigFactory(StandardAnalyzer standardAnalyzer) {
        this.standardAnalyzer = standardAnalyzer;
    }

    public IndexWriterConfig indexWriterConfig() {
        return new IndexWriterConfig(ApplicationConfiguration.version(), standardAnalyzer);
    }
}
