package com.openelements.showcases.analyzer.core.model;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Represents a text document to be analyzed.
 */
public record Document(Path path, String content) {

    public Document {
        Objects.requireNonNull(path, "path must not be null");
        Objects.requireNonNull(content, "content must not be null");
    }

    /**
     * Creates a document with the given content and a synthetic path.
     *
     * @param content the document content
     * @return a new Document instance
     */
    public static Document ofContent(String content) {
        return new Document(Path.of("unnamed"), content);
    }
}
