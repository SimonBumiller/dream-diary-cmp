package me.bumiller.ddcmp.domain.io

import me.bumiller.ddcmp.domain.model.ImportOption
import me.bumiller.ddcmp.domain.model.Settings

/**
 * Factory for creating backup importers.
 */
interface BackupImporterFactory {

    /**
     * Creates a new backup importer based on import options.
     */
    fun createImporter(options: ImportOption, settings: Settings): BackupImporter

}