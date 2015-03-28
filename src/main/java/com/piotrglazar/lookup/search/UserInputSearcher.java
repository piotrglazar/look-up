package com.piotrglazar.lookup.search;

import com.piotrglazar.lookup.domain.SearchResults;
import com.piotrglazar.lookup.engine.Searcher;
import com.piotrglazar.lookup.utils.ConsoleReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
public class UserInputSearcher {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String STOP = "q";

    private final ConsoleReader consoleReader;
    private final Searcher searcher;

    @Autowired
    public UserInputSearcher(ConsoleReader consoleReader, Searcher searcher) {
        this.consoleReader = consoleReader;
        this.searcher = searcher;
    }

    public void search() throws IOException {
        String input = "";
        while (shouldContinue(input)) {
            try {
                LOG.info("Enter the search query (q=quit):");
                input = consoleReader.readLine();
                if (!shouldContinue(input)) {
                    break;
                }

                final List<SearchResults> searchResults = searcher.searchInEnglish(input);

                LOG.info(String.format("Found %s hits.", searchResults.size()));
                searchResults.forEach(s -> {
                    LOG.info(String.format("%s. %s - %s (score=%s)", s.getIndex(), s.getEnglish(), s.getPolish(), s.getScore()));
                });
            } catch (Exception e) {
                LOG.error("Error searching {}", input, e);
            }
        }
    }

    private boolean shouldContinue(final String input) {
        return !input.equalsIgnoreCase(STOP);
    }
}
