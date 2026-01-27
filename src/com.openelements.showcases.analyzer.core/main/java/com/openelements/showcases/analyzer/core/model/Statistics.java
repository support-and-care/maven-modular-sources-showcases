package com.openelements.showcases.analyzer.core.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the analysis result of a document.
 */
public record Statistics(
        Document document,
        long lineCount,
        long wordCount,
        long characterCount,
        long characterCountWithoutSpaces,
        Map<String, Long> wordFrequencies,
        List<Map.Entry<String, Long>> topWords
) {
    public Statistics {
        Objects.requireNonNull(document, "document must not be null");
        Objects.requireNonNull(wordFrequencies, "wordFrequencies must not be null");
        Objects.requireNonNull(topWords, "topWords must not be null");
    }
}
