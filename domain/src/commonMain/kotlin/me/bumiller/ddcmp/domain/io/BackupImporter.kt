package me.bumiller.ddcmp.domain.io

import io.github.vinceglb.filekit.PlatformFile
import me.bumiller.ddcmp.domain.model.io.ImportResult

/**
 * Importer for backups.
 */
public interface BackupImporter {

    /**
     * Imports a given backup and returns the result.
     */
    public suspend fun importBackup(file: PlatformFile): ImportResult

}