package me.bumiller.ddcmp.domain.infrastructure

import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.PeriodicBackupOrder

/**
 * Repository granting access to a collection of periodic backup orders.
 */
interface PeriodicBackupOrderRepository: Repository<PeriodicBackupOrder> {

    /**
     * Contains the currently active order, or null if no order is active.
     */
    val activeOrder: Flow<PeriodicBackupOrder?>

}