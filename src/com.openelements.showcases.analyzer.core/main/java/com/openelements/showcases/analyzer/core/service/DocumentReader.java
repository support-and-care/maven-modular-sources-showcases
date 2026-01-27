package com.openelements.showcases.analyzer.core.service;

import com.openelements.showcases.analyzer.core.model.Document;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Service for reading documents from the filesystem.
 */
public class DocumentReader {

    private static final Logger LOG = LogManager.getLogger(DocumentReader.class);

    private final Charset charset;

    public DocumentReader() {
        this(StandardCharsets.UTF_8);
    }

    public DocumentReader(Charset charset) {
        this.charset = charset;
    }

    /**
     * Reads a document from the given path.
     *
     * @param path the path to the file
     * @return the document
     * @throws IOException if the file cannot be read
     */
    public Document read(Path path) throws IOException {
        LOG.info("Reading document from: {}", path);

        if (!Files.exists(path)) {
            throw new IOException("File not found: " + path);
        }

        if (!Files.isRegularFile(path)) {
            throw new IOException("Not a regular file: " + path);
        }

        String content = Files.readString(path, charset);
        LOG.debug("Read {} characters from {}", content.length(), path);

        return new Document(path, content);
    }
}
