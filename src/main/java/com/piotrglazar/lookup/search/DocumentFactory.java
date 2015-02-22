package com.piotrglazar.lookup.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.springframework.stereotype.Component;

@Component
public class DocumentFactory {

    public Document createDocument(String english, String polish) {
        Document document = new Document();
        document.add(new TextField("english", english, Field.Store.YES));
        document.add(new TextField("polish", polish, Field.Store.YES));

        return document;
    }
}
