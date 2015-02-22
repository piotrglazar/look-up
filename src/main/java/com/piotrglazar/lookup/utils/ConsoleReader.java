package com.piotrglazar.lookup.utils;

import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;

@Component
public class ConsoleReader implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final BufferedReader console = new BufferedReader(new InputStreamReader(System.in, Charsets.UTF_8));

    public String readLine() {
        try {
            return console.readLine();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read from console", e);
        }
    }

    @Override
    @PreDestroy
    public void close() {
        try {
            console.close();
        } catch (IOException e) {
            LOG.error("Failed to close console reader", e);
        }
    }
}
