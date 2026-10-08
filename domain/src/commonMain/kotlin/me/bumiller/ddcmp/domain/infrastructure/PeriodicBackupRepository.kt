package me.bumiller.ddcmp.domain.infrastructure

import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.backup.PeriodicBackup

/**
 * Repository granting access to a collection of periodic backup instances.
 */
interface PeriodicBackupRepository: Repository<PeriodicBackup> {

    /**
     * Gets all backups for a specific order.
     *
     * @param orderId The id of the backup order.
     */
    fun forOrder(orderId: Long): Flow<List<PeriodicBackup>>

}