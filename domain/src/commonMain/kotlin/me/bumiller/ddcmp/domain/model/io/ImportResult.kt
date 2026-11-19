package me.bumiller.ddcmp.domain.model.io

import me.bumiller.ddcmp.domain.model.meta.ProcessResult
import me.bumiller.ddcmp.domain.model.meta.Unique
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Result that gets created when importing a backup.
 */
public sealed interface ImportResult : Unique, ProcessResult {

    override val id: Long

    /**
     * The import failed.
     */
    public sealed interface Failure : ImportResult {

        /**
         * A problem occurred reading the file.
         */
        public data class FileAccess(override val id: Long, override val started: Instant,
                              override val ended: Instant) : Failure

        /**
         * The file did not adhere to the data format specified.
         */
        public data class FileFormat(override val id: Long, override val started: Instant,
                              override val ended: Instant) : Failure

        /**
         * Data consistency was not present, e.g. invalid references.
         */
        public data class DataConsistency(
            override val id: Long,
            override val started: Instant,
            override val ended: Instant
        ) : Failure

    }

    /**
     * The import was a success.
     */
    public sealed interface Success : ImportResult {

        /**
         * THe number of imported dreams.
         */
        public val dreamCount: Int

        /**
         * The number of imported persons.
         */
        public val personCount: Int

        /**
         * Timestamp of the first dream.
         */
        public val firstDream: Instant

        /**
         * Timestamp of the last dream.
         */
        public val lastDream: Instant

        /**
         * Normal import result.
         */
        public data class Normal(

            /**
             * The number of imported emotions
             */
            val emotionCount: Int,

            override val dreamCount: Int,
            override val personCount: Int,
            override val firstDream: Instant,
            override val lastDream: Instant,
            override val id: Long,
            override val started: Instant,
            override val ended: Instant

        ) : Success

        /**
         * Legacy import result.
         */
        public data class Legacy(
            override val dreamCount: Int,
            override val personCount: Int,
            override val firstDream: Instant,
            override val lastDream: Instant,
            override val id: Long,
            override val started: Instant,
            override val ended: Instant
        ) : Success

    }

}