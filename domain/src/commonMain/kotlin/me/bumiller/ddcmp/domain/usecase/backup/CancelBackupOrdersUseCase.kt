package me.bumiller.ddcmp.domain.usecase.backup

import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.domain.infrastructure.BackupScheduler
import me.bumiller.ddcmp.domain.infrastructure.PeriodicBackupOrderRepository
import me.bumiller.ddcmp.domain.usecase.meta.UseCase

/**
 * Use case that cancels all currently running backups.
 *
 * Returns if a backup was actually canceled.
 */
class CancelBackupOrdersUseCase(
    private val getOrder: GetBackupOrderUseCase,
    private val orderRepo: PeriodicBackupOrderRepository,
    private val backupScheduler: BackupScheduler
): UseCase<Unit, Boolean> {

    override suspend fun invoke(i: Unit): Boolean {
        val query = GetBackupOrderUseCase.I(id = null, active = true)
        val order = getOrder(query).first()
        val existsActiveOrder = order != null

        val isBackupRunning = backupScheduler.isBackupRunning()

        var dirty = false

        if(existsActiveOrder) {
            dirty = true
            val deactivatedOrder = order.copy(
                active = false
            )
            orderRepo.save(deactivatedOrder)
        }

        if(isBackupRunning) {
            dirty = true
            backupScheduler.cancelAll()
        }

        if(existsActiveOrder != isBackupRunning) {
            println("WARNING: Active order and backup scheduled was out of sync!")
        }

        return dirty
    }

}