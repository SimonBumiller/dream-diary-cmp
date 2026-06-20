package me.bumiller.ddcmp.domain.io

import io.github.vinceglb.filekit.PlatformFile
import me.bumiller.ddcmp.domain.model.ImportResult

/**
 * Importer for backups.
 */
interface BackupImporter {

    /**
     * Imports a given backup and returns the result.
     */
    suspend fun importBackup(file: PlatformFile): ImportResult

}