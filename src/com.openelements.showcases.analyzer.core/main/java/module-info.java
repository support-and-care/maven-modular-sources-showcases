/**
 * Core module for text analysis.
 * <p>
 * This module provides the domain model and analysis services.
 */
// tag::module-info[]
module com.openelements.showcases.analyzer.core {
    requires org.apache.logging.log4j; // <1>

    exports com.openelements.showcases.analyzer.core.model; // <2>
    exports com.openelements.showcases.analyzer.core.service; // <2>
}
// end::module-info[]
