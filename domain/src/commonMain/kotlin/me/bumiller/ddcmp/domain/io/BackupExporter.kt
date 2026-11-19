package me.bumiller.ddcmp.domain.io

import io.github.vinceglb.filekit.PlatformFile
import me.bumiller.ddcmp.domain.model.io.ExportResult

/**
 * Exports data.
 */
public interface BackupExporter {

    /**
     * Exports the data into a given file.
     *
     * @param file The file. Must be a file with `.zip` extension.
     */
    public suspend fun export(file: PlatformFile): ExportResult

}