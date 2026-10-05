package me.bumiller.ddcmp.domain.io

import me.bumiller.ddcmp.domain.model.ExportOption
import me.bumiller.ddcmp.domain.model.ImportOption
import me.bumiller.ddcmp.domain.model.Settings

/**
 * Factory for creating backup exporters.
 */
interface BackupExporterFactory {

    /**
     * Creates a new backup exporter based on import options.
     */
    fun createImporter(options: ExportOption): BackupExporter

}