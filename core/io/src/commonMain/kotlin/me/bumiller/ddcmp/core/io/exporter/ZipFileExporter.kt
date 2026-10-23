package me.bumiller.ddcmp.core.io.exporter

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.extension
import io.github.vinceglb.filekit.path
import kotlinx.io.IOException
import kotlinx.io.buffered
import me.bumiller.ddcmp.core.io.common.countingSink
import me.bumiller.ddcmp.common.captureException
import me.bumiller.ddcmp.domain.io.BackupExporter
import me.bumiller.ddcmp.common.asSinkFixed
import me.bumiller.ddcmp.domain.model.io.ExportResult
import no.synth.kmpzip.kotlinx.ZipOutputStream
import no.synth.kmpzip.zip.ZipOutputStream
import kotlin.time.Clock

private const val ZIP_EXTENSION = "zip"

/**
 * Base class for any exporter that exports the data into a zip file of several other files.
 */
internal abstract class ZipFileExporter: BackupExporter {

    override suspend fun export(file: PlatformFile): ExportResult {
        require(file.extension == ZIP_EXTENSION) { "Data can only be exported to a .zip file." }

        val startTimestamp = Clock.System.now()

        val rawSink = file.asSinkFixed().countingSink()

        val result = rawSink.buffered().use { sink ->
            ZipOutputStream(sink).use { zipStream ->
                try {
                    aggregateZipFile(zipStream)
                } catch (e: IOException) {
                    captureException(e)
                    ExportResult.Failure.FileAccess(0, startTimestamp, Clock.System.now())
                }
            }
        }

        if(result != null) return result

        return ExportResult.Success(
            id = 0,
            started = startTimestamp,
            fileSize = rawSink.writtenByteCount(),
            fileLocation = file.path,
            ended = Clock.System.now()
        )
    }

    /**
     * Aggregate the different zip file contents.
     *
     * @param zipStream The zip stream into the zip file.
     */
    protected abstract suspend fun aggregateZipFile(zipStream: ZipOutputStream): ExportResult?

}