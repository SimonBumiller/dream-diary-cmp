package me.bumiller.ddcmp.core.io.exporter

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.extension
import io.github.vinceglb.filekit.path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.io.buffered
import me.bumiller.ddcmp.common.asSinkFixed
import me.bumiller.ddcmp.core.io.common.countingSink
import me.bumiller.ddcmp.core.io.common.use
import me.bumiller.ddcmp.domain.io.BackupExporter
import me.bumiller.ddcmp.domain.model.io.ExportResult
import no.synth.kmpzip.kotlinx.ZipOutputStream
import no.synth.kmpzip.zip.ZipOutputStream
import kotlin.time.Clock

/**
 * Base class for any exporter that exports the data into a zip file of several other files.
 */
internal class ZipFileExporter(
    private val dataStrategy: DataExportStrategy
) : BackupExporter {

    override suspend fun export(file: PlatformFile): ExportResult = withContext(Dispatchers.IO) {
        require(file.extension == ZIP_EXTENSION) { "Data can only be exported to a .zip file." }
        val startTimestamp = Clock.System.now()

        // Create the sink to the file and the zip file into it.
        val fileSink = file.asSinkFixed().countingSink()
        val zipStream = ZipOutputStream(fileSink.buffered())

        // Apply the data strategy on the zip stream.
        val result = zipStream.use(dataStrategy::writeDataInto)

        when (result) {
            DataExportStrategy.DataResult.Success -> ExportResult.Success(
                id = 0L,
                started = startTimestamp,
                ended = Clock.System.now(),
                fileSize = fileSink.writtenByteCount(),
                fileLocation = file.path
            )

            is DataExportStrategy.DataResult.WriteError -> ExportResult.Failure.FileAccess(
                id = 0L,
                started = startTimestamp,
                ended = Clock.System.now()
            )
        }
    }

    companion object {
        private const val ZIP_EXTENSION = "zip"
    }

    /**
     * Strategy to export the data into a zip output-stream.
     */
    internal interface DataExportStrategy {

        /**
         * Writes the user-data into a zip output-stream.
         *
         * This method does not throw any exceptions. Exceptions are handles and reflected through the return value.
         *
         * @param zipStream The output-stream
         */
        suspend fun writeDataInto(zipStream: ZipOutputStream): DataResult

        /**
         * Result of the data export action.
         */
        sealed interface DataResult {

            /**
             * Data was written successfully.
             */
            data object Success : DataResult

            /**
             * A problem occurred when writing.
             */
            data class WriteError(val exception: IOException) : DataResult

        }

    }

}