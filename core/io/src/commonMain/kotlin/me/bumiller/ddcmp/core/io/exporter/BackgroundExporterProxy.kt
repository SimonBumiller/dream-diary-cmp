package me.bumiller.ddcmp.core.io.exporter

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import me.bumiller.ddcmp.domain.io.BackupExporter
import me.bumiller.ddcmp.domain.model.io.ExportResult

// Proxy that switches to the Dispatchers.IO context during exporting.
internal class BackgroundExporterProxy(
    private val delegate: BackupExporter
) : BackupExporter {
    override suspend fun export(file: PlatformFile): ExportResult
    = withContext(Dispatchers.IO) {
        delegate.export(file)
    }
}