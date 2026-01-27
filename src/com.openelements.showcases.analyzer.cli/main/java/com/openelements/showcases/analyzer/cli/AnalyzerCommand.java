package com.openelements.showcases.analyzer.cli;

import com.openelements.showcases.analyzer.core.model.Document;
import com.openelements.showcases.analyzer.core.model.Statistics;
import com.openelements.showcases.analyzer.core.service.DocumentReader;
import com.openelements.showcases.analyzer.core.service.TextAnalyzer;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Main CLI command for the text analyzer.
 */
@Command(
        name = "analyzer",
        mixinStandardHelpOptions = true,
        version = "Text Analyzer 1.0.0",
        description = "Analyzes text files and provides statistics."
)
public class AnalyzerCommand implements Callable<Integer> {

    @Parameters(
            index = "0",
            description = "The text file to analyze"
    )
    private Path file;

    @Option(
            names = {"-t", "--top"},
            description = "Number of top words to display (default: ${DEFAULT-VALUE})",
            defaultValue = "10"
    )
    private int topWords;

    @Option(
            names = {"-f", "--frequencies"},
            description = "Show all word frequencies"
    )
    private boolean showAllFrequencies;

    @Option(
            names = {"-q", "--quiet"},
            description = "Quiet mode - only show numbers"
    )
    private boolean quiet;

    @Override
    public Integer call() {
        try {
            DocumentReader reader = new DocumentReader();
            Document document = reader.read(file);

            TextAnalyzer analyzer = new TextAnalyzer(topWords);
            Statistics stats = analyzer.analyze(document);

            printResults(stats);
            return 0;

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return 1;
        }
    }

    private void printResults(Statistics stats) {
        if (quiet) {
            printQuietResults(stats);
        } else {
            printDetailedResults(stats);
        }
    }

    private void printQuietResults(Statistics stats) {
        System.out.printf("%d %d %d%n",
                stats.lineCount(),
                stats.wordCount(),
                stats.characterCount());
    }

    private void printDetailedResults(Statistics stats) {
        System.out.println("=== Text Analysis Results ===");
        System.out.println();
        System.out.println("File: " + stats.document().path());
        System.out.println();
        System.out.println("--- Basic Statistics ---");
        System.out.printf("Lines:                    %,d%n", stats.lineCount());
        System.out.printf("Words:                    %,d%n", stats.wordCount());
        System.out.printf("Characters:               %,d%n", stats.characterCount());
        System.out.printf("Characters (no spaces):   %,d%n", stats.characterCountWithoutSpaces());
        System.out.println();

        System.out.println("--- Top " + topWords + " Words ---");
        int rank = 1;
        for (Map.Entry<String, Long> entry : stats.topWords()) {
            System.out.printf("%2d. %-20s %,d%n", rank++, entry.getKey(), entry.getValue());
        }

        if (showAllFrequencies) {
            System.out.println();
            System.out.println("--- All Word Frequencies ---");
            stats.wordFrequencies().entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .forEach(e -> System.out.printf("%-20s %,d%n", e.getKey(), e.getValue()));
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new AnalyzerCommand()).execute(args);
        System.exit(exitCode);
    }
}
