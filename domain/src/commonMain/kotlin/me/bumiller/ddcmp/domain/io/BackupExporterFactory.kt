package me.bumiller.ddcmp.domain.io

import me.bumiller.ddcmp.domain.model.io.ExportOption

/**
 * Factory for creating backup exporters.
 */
interface BackupExporterFactory {

    /**
     * Creates a new backup exporter based on import options.
     */
    fun createExporter(options: ExportOption): BackupExporter

}