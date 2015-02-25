package com.piotrglazar.lookup;

import com.piotrglazar.lookup.configuration.ApplicationConfiguration;
import com.piotrglazar.lookup.search.UserInputSearcher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public final class LookUpConsole {

    private LookUpConsole() {
        // Utility class
    }

    public static void main(String[] args) throws IOException {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

        final UserInputSearcher userInputSearcher = context.getBean(UserInputSearcher.class);
        userInputSearcher.search();
    }
}
