package me.bumiller.ddcmp.core.io.importer

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import me.bumiller.ddcmp.core.io.common.asSeekableSource
import me.bumiller.ddcmp.core.io.common.use
import me.bumiller.ddcmp.domain.io.BackupImporter
import me.bumiller.ddcmp.domain.model.io.ImportResult
import no.synth.kmpzip.zip.ZipFile
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * Importer that imports from a zip-file.
 */
internal class ZipFileImporter(
    private val strategy: DataImportStrategy
) : BackupImporter {

    override suspend fun importBackup(file: PlatformFile): ImportResult =
        withContext(Dispatchers.IO) {
            val started = Clock.System.now()
            val seekableFile = file.asSeekableSource()

            val result = ZipFile(seekableFile).use(strategy::readDataFrom)

            val finished = Clock.System.now()

            return@withContext when (result) {
                DataImportStrategy.DataResult.DataFormat -> ImportResult.Failure.FileFormat(
                    0L,
                    started,
                    finished
                )

                DataImportStrategy.DataResult.DataIntegrity -> ImportResult.Failure.DataConsistency(
                    0L,
                    started,
                    finished
                )

                DataImportStrategy.DataResult.ReadError -> ImportResult.Failure.FileFormat(
                    0L,
                    started,
                    finished
                )

                is DataImportStrategy.DataResult.Success -> result.run {
                    ImportResult.Success.Normal(
                        emotionCount,
                        dreamCount,
                        personCount,
                        firstDream,
                        lastDream,
                        0L,
                        started,
                        finished
                    )
                }
            }
        }

    /**
     * Strategy to import the data from a zip input-stream.
     */
    internal interface DataImportStrategy {

        /**
         * Reads the user-data from a zip-file.
         *
         * This method does not throw any exceptions. Exceptions are handled and reflected through the return value.
         *
         * @param zipFile The zip-file.
         */
        suspend fun readDataFrom(zipFile: ZipFile): DataResult

        /**
         * Result of the data import action.
         */
        sealed interface DataResult {

            /**
             * Data was read successfully.
             */
            data class Success(
                val emotionCount: Int,
                val dreamCount: Int,
                val personCount: Int,
                val firstDream: Instant,
                val lastDream: Instant
            ) : DataResult

            /**
             * A problem occurred when reading the data.
             */
            data object ReadError : DataResult

            /**
             * A problem occurred when serializing the data.
             */
            data object DataFormat : DataResult

            /**
             * A problem occurred when verifying the data integrity.
             */
            data object DataIntegrity : DataResult

        }
    }
}