package com.piotrglazar.lookup.domain;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.springframework.stereotype.Component;

import static com.piotrglazar.lookup.domain.LookUpDocument.ENGLISH_FIELD;
import static com.piotrglazar.lookup.domain.LookUpDocument.POLISH_FIELD;

@Component
public class DocumentFactory {

    public LookUpDocument createDocument(String english, String polish) {
        Document document = new Document();
        document.add(new TextField(ENGLISH_FIELD, english, Field.Store.YES));
        document.add(new TextField(POLISH_FIELD, polish, Field.Store.YES));

        return new LookUpDocument(document);
    }


}
