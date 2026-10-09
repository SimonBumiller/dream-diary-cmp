package me.bumiller.ddcmp.domain.usecase.backup

import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.domain.infrastructure.BackupScheduler
import me.bumiller.ddcmp.domain.infrastructure.PeriodicBackupOrderRepository
import me.bumiller.ddcmp.domain.model.backup.PeriodicBackupOrder
import me.bumiller.ddcmp.domain.usecase.meta.UseCase

class ScheduleBackupOrderUseCase(
    private val backupScheduler: BackupScheduler,
    private val cancelBackup: CancelBackupOrdersUseCase,
    private val orderRepo: PeriodicBackupOrderRepository
) : UseCase<Long, PeriodicBackupOrder?> {

    override suspend fun invoke(i: Long): PeriodicBackupOrder? {
        // Ensure no backup is active right now
        if (cancelBackup(Unit)) {
            println("INFO: Backup was scheduled while another one was still running")
        }

        val order = orderRepo.id(i)
            .first() ?: error("Didn't find order for id $i")

        val success = backupScheduler.scheduleOrder(order)

        // We mark the order as active, now that is is scheduled
        val activatedOrder = order.copy(
            active = true,
            wasActive = true
        )
        orderRepo.save(activatedOrder)

        return if (!success) {
            // If the backup wasn't scheduled, we immediately delete the newly created order.
            cancelBackup(Unit)
            null
        } else {
            order
        }
    }

}