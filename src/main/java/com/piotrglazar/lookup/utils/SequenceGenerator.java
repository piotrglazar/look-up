package com.piotrglazar.lookup.utils;

public class SequenceGenerator {

    private int seed;

    public int next() {
        return ++seed;
    }
}
