package me.bumiller.ddcmp.domain.io

import me.bumiller.ddcmp.domain.model.io.ExportOption

/**
 * Factory for creating backup exporters.
 */
public interface BackupExporterFactory {

    /**
     * Creates a new backup exporter based on import options.
     */
    public fun createExporter(options: ExportOption): BackupExporter

}