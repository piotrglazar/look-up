package com.piotrglazar.lookup.utils;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ByteArrayToStringListConverter {

    public List<String> convert(byte[] bytes) {
        String content = new String(bytes, Charsets.UTF_8);
        content = content.replaceAll("\\r", "");
        return Splitter.on("\n").splitToList(content);
    }
}
