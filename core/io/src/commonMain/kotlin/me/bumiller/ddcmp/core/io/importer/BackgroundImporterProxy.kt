package me.bumiller.ddcmp.core.io.importer

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import me.bumiller.ddcmp.domain.io.BackupImporter
import me.bumiller.ddcmp.domain.model.ImportResult

// Proxy that switches to the Dispatchers.IO context during importing.
private class BackgroundImporterProxy(
    private val delegate: BackupImporter
) : BackupImporter {
    override suspend fun importBackup(file: PlatformFile): ImportResult =
        withContext(Dispatchers.IO) {
            delegate.importBackup(file)
        }
}

internal fun BackupImporter.backgroundProxy(): BackupImporter = BackgroundImporterProxy(this)