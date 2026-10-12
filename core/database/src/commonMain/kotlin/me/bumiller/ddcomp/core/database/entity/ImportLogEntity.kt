package me.bumiller.ddcomp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.bumiller.ddcomp.core.database.entity.base.BaseEntity
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Entity class for a log entry when importing data.
 */
@OptIn(ExperimentalTime::class)
@Entity(
    tableName = "import_logs"
)
data class ImportLogEntity (

    @PrimaryKey(autoGenerate = true)
    override val id: Long,

    override val created: Instant,

    override val updated: Instant,

    val ended: Instant,

    /**
     * Whether failed due to file access
     */
    val isFileAccessFailure: Boolean,

    /**
     * Whether it failed due to file format
     */
    val isFileFormatFailure: Boolean,

    /**
     * Whether it failed due to data consistency
     */
    val isDataConsistencyFailure: Boolean,

    /**
     * The count of imported dreams
     */
    val dreamCount: Int,

    /**
     * The count of imported persons
     */
    val personCount: Int,

    /**
     * The first dream timestamp
     */
    val firstDream: Instant,

    /**
     * The last dream timestamp
     */
    val lastDream: Instant,

    /**
     * Whether the import was done by importing a normal file
     */
    val isNormal: Boolean,

    /**
     * The count of imported emotions
     */
    val emotionCount: Int

) : BaseEntity