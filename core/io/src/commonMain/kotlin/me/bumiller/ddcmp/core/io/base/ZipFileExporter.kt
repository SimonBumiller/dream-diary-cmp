package me.bumiller.ddcmp.core.io.base

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.extension
import io.github.vinceglb.filekit.sink
import kotlinx.io.buffered
import me.bumiller.ddcmp.domain.io.BackupExporter
import me.bumiller.ddcmp.domain.model.ExportResult
import no.synth.kmpzip.kotlinx.ZipOutputStream
import no.synth.kmpzip.zip.ZipEntry
import no.synth.kmpzip.zip.ZipOutputStream

private const val ZIP_EXTENSION = "zip"

/**
 * Base class for any exporter that exports the data into a zip file of several other files.
 */
internal abstract class ZipFileExporter: BackupExporter {

    override suspend fun export(file: PlatformFile): ExportResult {
        require(file.extension == ZIP_EXTENSION) { "Data can only be exported to a .zip file." }

        file.sink(append = false).buffered().use { sink ->
            ZipOutputStream(sink).use { zipStream ->
                return aggregateZipFile(zipStream)
            }
        }
    }

    protected fun ZipOutputStream.entryForName(
        name: String,
        extension: String,
        block: ZipOutputStream.() -> Unit
    ) {
        val entry = ZipEntry("$name.$extension")

        putNextEntry(entry)
        try {
            block()
        } finally {
            closeEntry()
        }
    }

    /**
     * Aggregate the different zip file contents.
     *
     * @param zipStream The zip stream into the zip file.
     */
    protected abstract suspend fun aggregateZipFile(zipStream: ZipOutputStream): ExportResult

}