package me.bumiller.ddcmp.domain.model.io

import me.bumiller.ddcmp.domain.model.meta.ProcessResult
import me.bumiller.ddcmp.domain.model.meta.Unique
import kotlin.time.Instant

/**
 * Result when exporting data.
 */
sealed interface ExportResult: Unique, ProcessResult {

    override val id: Long

    /**
     * Data was exported successfully.
     */
    data class Success(
        override val id: Long,
        override val started: Instant,
        override val ended: Instant,

        /**
         * Size of the export file in bytes.
         */
        val fileSize: Long,

        /**
         * The location of the export file.
         */
        val fileLocation: String

    ): ExportResult

    /**
     * Failure arose when exporting.
     */
    sealed interface Failure: ExportResult {

        /**
         * The file could not be accessed.
         */
        data class FileAccess(override val id: Long, override val started: Instant,
                              override val ended: Instant): Failure

    }

}