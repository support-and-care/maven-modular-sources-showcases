package com.openelements.showcases.analyzer.core.service;

import com.openelements.showcases.analyzer.core.model.Document;
import com.openelements.showcases.analyzer.core.model.Statistics;
// tag::import-internal[]
import com.openelements.showcases.analyzer.core.internal.TextNormalizer;
// end::import-internal[]
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service for analyzing text documents.
 */
public class TextAnalyzer {

    private static final Logger LOG = LogManager.getLogger(TextAnalyzer.class);

    private final int topWordsLimit;

    public TextAnalyzer() {
        this(10);
    }

    public TextAnalyzer(int topWordsLimit) {
        this.topWordsLimit = topWordsLimit;
    }

    /**
     * Analyzes the given document and returns statistics.
     *
     * @param document the document to analyze
     * @return the analysis statistics
     */
    public Statistics analyze(Document document) {
        LOG.info("Analyzing document: {}", document.path());

        String content = document.content();
        String[] lines = content.split("\\R");
        long lineCount = lines.length;

        // tag::use-normalizer[]
        String[] words = TextNormalizer.tokenize(content);
        // end::use-normalizer[]
        long wordCount = words.length;

        long characterCount = content.length();
        long characterCountWithoutSpaces = content.chars()
                .filter(c -> !Character.isWhitespace(c))
                .count();

        Map<String, Long> wordFrequencies = Arrays.stream(words)
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Map.Entry<String, Long>> topWords = wordFrequencies.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(topWordsLimit)
                .toList();

        LOG.debug("Analysis complete: {} lines, {} words, {} characters",
                lineCount, wordCount, characterCount);

        return new Statistics(
                document,
                lineCount,
                wordCount,
                characterCount,
                characterCountWithoutSpaces,
                wordFrequencies,
                topWords
        );
    }

}
