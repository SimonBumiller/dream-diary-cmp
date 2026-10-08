package me.bumiller.ddcmp.domain.infrastructure

import me.bumiller.ddcmp.domain.model.backup.PeriodicBackupOrder

/**
 * Interface to schedule backups.
 */
interface BackupScheduler {

    /**
     * Schedules backups for a specified order.
     *
     * @return Whether the schedule was successful.
     */
    suspend fun scheduleOrder(order: PeriodicBackupOrder): Boolean

    /**
     * Cancels all currently ongoing backup tasks.
     *
     * @return The number of stopped backups orders.
     */
    suspend fun cancelAll(): Int

}