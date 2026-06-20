package me.bumiller.ddcmp.domain.io

import io.github.vinceglb.filekit.PlatformFile
import me.bumiller.ddcmp.domain.model.ExportResult

/**
 * Exports data.
 */
interface BackupExporter {

    /**
     * Exports the data.
     */
    suspend fun export(file: PlatformFile): ExportResult

}