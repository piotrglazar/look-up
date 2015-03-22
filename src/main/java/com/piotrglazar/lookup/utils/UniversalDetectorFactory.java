package com.piotrglazar.lookup.utils;

import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.stereotype.Component;

@Component
public class UniversalDetectorFactory {

    public UniversalDetector getUniversalDetector() {
        return new UniversalDetector(null);
    }
}
