package com.openelements.showcases.analyzer.core.service;

import com.openelements.showcases.analyzer.core.model.Document;
import com.openelements.showcases.analyzer.core.model.Statistics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service for analyzing text documents.
 */
public class TextAnalyzer {

    private static final Logger LOG = LogManager.getLogger(TextAnalyzer.class);
    private static final Pattern WORD_PATTERN = Pattern.compile("\\s+");
    private static final Pattern NON_WORD_CHARS = Pattern.compile("[^\\p{L}\\p{N}]");

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

        String[] words = extractWords(content);
        long wordCount = words.length;

        long characterCount = content.length();
        long characterCountWithoutSpaces = content.chars()
                .filter(c -> !Character.isWhitespace(c))
                .count();

        Map<String, Long> wordFrequencies = Arrays.stream(words)
                .filter(word -> !word.isEmpty())
                .map(String::toLowerCase)
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

    private String[] extractWords(String content) {
        return WORD_PATTERN.splitAsStream(content)
                .map(word -> NON_WORD_CHARS.matcher(word).replaceAll(""))
                .filter(word -> !word.isEmpty())
                .toArray(String[]::new);
    }
}
