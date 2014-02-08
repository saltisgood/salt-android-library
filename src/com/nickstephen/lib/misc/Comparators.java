package com.nickstephen.lib.misc;

import java.util.Comparator;

/**
 * Created by Nick on 30/11/13.
 */
public class Comparators {
    private Comparators() {}

    public static final class StringCompareCaseIgnore implements Comparator<String> {
        @Override
        public int compare(String s, String s2) {
            return s.toLowerCase().compareTo(s2.toLowerCase());
        }
    }
}
