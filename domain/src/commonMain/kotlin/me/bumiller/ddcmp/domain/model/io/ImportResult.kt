@file:OptIn(ExperimentalTime::class)

package me.bumiller.ddcmp.domain.model.io

import me.bumiller.ddcmp.domain.model.meta.Unique
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Result that gets created when importing a backup.
 */
sealed interface ImportResult : Unique {

    override val id: Long

    /**
     * The timestamp when the import was requested.
     */
    val timestamp: Instant

    /**
     * The import failed.
     */
    sealed interface Failure : ImportResult {

        /**
         * A problem occurred reading the file.
         */
        data class FileAccess(override val id: Long, override val timestamp: Instant) : Failure

        /**
         * The file did not adhere to the data format specified.
         */
        data class FileFormat(override val id: Long, override val timestamp: Instant) : Failure

        /**
         * Data consistency was not present, e.g. invalid references.
         */
        data class DataConsistency(
            override val id: Long,
            override val timestamp: Instant
        ) : Failure

    }

    /**
     * The import was a success.
     */
    sealed interface Success : ImportResult {

        /**
         * THe number of imported dreams.
         */
        val dreamCount: Int

        /**
         * The number of imported persons.
         */
        val personCount: Int

        /**
         * Timestamp of the first dream.
         */
        val firstDream: Instant

        /**
         * Timestamp of the last dream.
         */
        val lastDream: Instant

        /**
         * Normal import result.
         */
        data class Normal(

            /**
             * The number of imported emotions
             */
            val emotionCount: Int,

            override val dreamCount: Int,
            override val personCount: Int,
            override val firstDream: Instant,
            override val lastDream: Instant,
            override val id: Long,
            override val timestamp: Instant

        ) : Success

        /**
         * Legacy import result.
         */
        data class Legacy(
            override val dreamCount: Int,
            override val personCount: Int,
            override val firstDream: Instant,
            override val lastDream: Instant,
            override val id: Long,
            override val timestamp: Instant
        ) : Success

    }

}