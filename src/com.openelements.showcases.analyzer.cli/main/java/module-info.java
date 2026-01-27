/**
 * CLI module for text analysis.
 * <p>
 * This module provides a command-line interface for the text analyzer.
 */
// tag::module-info[]
module com.openelements.showcases.analyzer.cli {
    requires com.openelements.showcases.analyzer.core;
    requires info.picocli;
    requires org.apache.logging.log4j;

    opens com.openelements.showcases.analyzer.cli to info.picocli;
}
// end::module-info[]
