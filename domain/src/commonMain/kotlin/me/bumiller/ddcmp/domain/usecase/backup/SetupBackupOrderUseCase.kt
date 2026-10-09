package me.bumiller.ddcmp.domain.usecase.backup

import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.domain.infrastructure.BackupScheduler
import me.bumiller.ddcmp.domain.infrastructure.PeriodicBackupOrderRepository
import me.bumiller.ddcmp.domain.model.backup.BackupDestination
import me.bumiller.ddcmp.domain.model.backup.PeriodicBackupOrder
import me.bumiller.ddcmp.domain.model.io.ExportOption
import me.bumiller.ddcmp.domain.usecase.backup.SetupBackupOrderUseCase.I
import me.bumiller.ddcmp.domain.usecase.meta.UseCase
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

class SetupBackupOrderUseCase(
    private val backupScheduler: BackupScheduler,
    private val cancelBackup: CancelBackupOrdersUseCase,
    private val orderRepo: PeriodicBackupOrderRepository
) : UseCase<I, PeriodicBackupOrder?> {

    /**
     * The attributes needed to construct a new backup order.
     */
    data class I(

        val destination: BackupDestination,
        val period: Duration,
        val initialTime: Instant,
        val keepLast: Int?,
        val exportOption: ExportOption

    )

    override suspend fun invoke(i: I): PeriodicBackupOrder? {
        // Ensure no backup is active right now
        if (cancelBackup(Unit)) {
            println("INFO: Backup was scheduled while another one was still running")
        }

        val newOrder = PeriodicBackupOrder(
            id = 0,
            created = Clock.System.now(),
            destination = i.destination,
            active = true,
            period = i.period,
            initialTime = i.initialTime,
            keepLast = i.keepLast,
            exportOption = i.exportOption
        ).let { orderRepo.save(it) }
            .let { orderRepo.id(it).first() ?: error("Didn't find newly created backup order") }

        val success = backupScheduler.scheduleOrder(newOrder)

        return if(!success) {
            // If the backup wasn't scheduled, we immediately delete the newly created order.
            cancelBackup(Unit)
            null
        } else {
            newOrder
        }
    }

}