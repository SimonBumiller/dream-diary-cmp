package me.bumiller.ddcmp.domain.infrastructure

import me.bumiller.ddcmp.domain.model.backup.PeriodicBackupOrder

/**
 * Interface to schedule backups.
 */
public interface BackupScheduler {

    /**
     * Schedules backups for a specified order.
     *
     * @return Whether the schedule was successful.
     */
    public suspend fun scheduleOrder(order: PeriodicBackupOrder): Boolean

    /**
     * Cancels all currently ongoing backup tasks.
     *
     * @return The number of stopped backups orders.
     */
    public suspend fun cancelAll(): Int

    /**
     * Whether a backup is running at the moment
     */
    public suspend fun isBackupRunning(): Boolean

}