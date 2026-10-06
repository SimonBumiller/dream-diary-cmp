package me.bumiller.ddcomp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.bumiller.ddcomp.core.database.entity.base.BaseEntity
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Entity class for a log entry when exporting data.
 */
@OptIn(ExperimentalTime::class)
@Entity(
    tableName = "export_logs"
)
data class ExportLogEntity (

    @PrimaryKey(autoGenerate = true)
    override val id: Long,

    override val created: Instant,

    override val updated: Instant,

    /**
     * Whether failed due to file access
     */
    val isFileAccessFailure: Boolean,

    /**
     * The export file size.
     */
    val fileSize: Long?,

    /**
     * The export file location.
     */
    val fileLocation: String?

) : BaseEntity