package me.bumiller.ddcmp.core.io.exporter

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.extension
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.sink
import io.github.vinceglb.filekit.size
import kotlinx.io.IOException
import kotlinx.io.buffered
import me.bumiller.ddcmp.core.io.common.countingSink
import me.bumiller.ddcmp.domain.io.BackupExporter
import me.bumiller.ddcmp.domain.model.ExportResult
import no.synth.kmpzip.kotlinx.ZipOutputStream
import no.synth.kmpzip.zip.ZipEntry
import no.synth.kmpzip.zip.ZipOutputStream
import kotlin.time.Clock

private const val ZIP_EXTENSION = "zip"

/**
 * Base class for any exporter that exports the data into a zip file of several other files.
 */
internal abstract class ZipFileExporter: BackupExporter {

    override suspend fun export(file: PlatformFile): ExportResult {
        require(file.extension == ZIP_EXTENSION) { "Data can only be exported to a .zip file." }

        val now = Clock.System.now()

        val rawSink = file.sink(append = false).countingSink()

        val result = rawSink.buffered().use { sink ->
            ZipOutputStream(sink).use { zipStream ->
                try {
                    aggregateZipFile(zipStream)
                } catch (_: IOException) {
                    ExportResult.Failure.FileAccess(0, now)
                }
            }
        }

        if(result != null) return result

        return ExportResult.Success(
            id = 0,
            timestamp = now,
            fileSize = rawSink.writtenByteCount(),
            fileLocation = file.path
        )
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
    protected abstract suspend fun aggregateZipFile(zipStream: ZipOutputStream): ExportResult?

}