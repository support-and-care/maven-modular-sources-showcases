package com.openelements.showcases.analyzer.core.internal;

import java.util.regex.Pattern;

/**
 * Internal utility for text normalization.
 * This class is NOT exported and cannot be used by other modules.
 */
// tag::class[]
public class TextNormalizer {

    private static final Pattern NON_WORD_CHARS = Pattern.compile("[^\\p{L}\\p{N}]");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

    // tag::normalize[]
    public static String normalize(String text) {
        String cleaned = NON_WORD_CHARS.matcher(text).replaceAll(" ");
        return WHITESPACE.matcher(cleaned).replaceAll(" ").trim().toLowerCase();
    }
    // end::normalize[]

    // tag::tokenize[]
    public static String[] tokenize(String text) {
        String normalized = normalize(text);
        if (normalized.isEmpty()) {
            return new String[0];
        }
        return WHITESPACE.split(normalized);
    }
    // end::tokenize[]
}
// end::class[]
