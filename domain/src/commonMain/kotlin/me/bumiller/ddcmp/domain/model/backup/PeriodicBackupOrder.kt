package me.bumiller.ddcmp.domain.model.backup

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.io.ExportOption
import me.bumiller.ddcmp.domain.model.meta.Trackable
import me.bumiller.ddcmp.domain.model.meta.Unique
import kotlin.time.Duration
import kotlin.time.Instant

/**
 * Contains instructions to specify a periodic backup.
 */
data class PeriodicBackupOrder(

    override val id: Long,
    override val created: Instant,

    /**
     * The destination into which the backups should be written.
     */
    val destination: BackupDestination,

    /**
     * Whether this backup order is currently active, i.e. being used.
     */
    val active: Boolean,

    /**
     * Whether the backup order has been scheduled for backup yet.
     */
    val wasActive: Boolean,

    /**
     * The period on which the next backup time is determined.
     */
    val period: Duration,

    /**
     * The timestamp when the first backup is performed.
     */
    val initialTime: Instant,

    /**
     * How many of the recent backups should be kept stored.
     * Null if there is no limit.
     */
    val keepLast: Int?,

    /**
     * Configuration of the export.
     */
    val exportOption: ExportOption

) : Unique, Trackable {

    /**
     * Creates a filename for a backup for this order.
     *
     * TODO: Make this dynamic.
     *
     * @param timestamp The timestamp on when the backup is performed.
     */
    fun createBackupFileName(timestamp: Instant) = "Backup-$id-${
        timestamp.toLocalDateTime(TimeZone.currentSystemDefault()).toBackupFilenameRepresentation()
    }.zip"

    private fun LocalDateTime.toBackupFilenameRepresentation() =
        "${date.day}-${date.month.ordinal}-${date.year}_${time.hour}-${time.minute}-${time.second}"

    init {
        val periodLongEnough = period.inWholeMinutes >= 30
        require(periodLongEnough) { "BackupOrders require at least 30 minute durations." }

        val keepLastInBounds = keepLast == null || keepLast > 2
        require(keepLastInBounds) { "BackupOrders requires to keep at least 3 backups or unlimited." }
    }

}