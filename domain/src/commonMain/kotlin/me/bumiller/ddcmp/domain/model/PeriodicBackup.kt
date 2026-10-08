package me.bumiller.ddcmp.domain.model

import me.bumiller.ddcmp.domain.model.meta.Trackable
import me.bumiller.ddcmp.domain.model.meta.Unique
import kotlin.time.Instant

/**
 * Models an export that was performed for a [PeriodicBackupOrder].
 */
data class PeriodicBackup(

    override val id: Long,
    override val created: Instant,

    /**
     * The order for which the backup was created.
     */
    val order: PeriodicBackupOrder,

    /**
     * The result of this backups export.
     */
    val export: ExportResult

): Unique, Trackable
