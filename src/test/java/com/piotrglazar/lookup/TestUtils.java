package com.piotrglazar.lookup;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

public final class TestUtils {

    private TestUtils() {
        // Utility class
    }

    public static ClassPathResource classPathResource(String path) {
        return new ClassPathResource(path);
    }

    public static FileSystemResource fileSystemResource(String filename) {
        return new FileSystemResource(userCurrentDirectory() + "/externalResource/" + filename);
    }

    private static String userCurrentDirectory() {
        return System.getProperty("user.dir");
    }
}
