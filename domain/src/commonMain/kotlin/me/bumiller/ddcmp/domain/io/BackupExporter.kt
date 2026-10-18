package me.bumiller.ddcmp.domain.io

import io.github.vinceglb.filekit.PlatformFile
import me.bumiller.ddcmp.domain.model.io.ExportResult

/**
 * Exports data.
 */
interface BackupExporter {

    /**
     * Exports the data into a given file.
     *
     * @param file The file. Must be a file with `.zip` extension.
     */
    suspend fun export(file: PlatformFile): ExportResult

    /**
     * Deletes as many backup files inside [directory] so that at a maximum [maxCount] backup files are present.
     *
     * Assumes that backup files are the only files inside the directory.
     *
     * @return True if files were deleted.
     */
    suspend fun ensureMaxBackupCount(directory: PlatformFile, maxCount: Int): Boolean

}