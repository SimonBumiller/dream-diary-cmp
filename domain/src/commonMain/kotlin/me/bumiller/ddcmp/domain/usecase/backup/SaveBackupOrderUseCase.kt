package me.bumiller.ddcmp.domain.usecase.backup

import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.domain.infrastructure.BackupScheduler
import me.bumiller.ddcmp.domain.infrastructure.PeriodicBackupOrderRepository
import me.bumiller.ddcmp.domain.model.backup.BackupDestination
import me.bumiller.ddcmp.domain.model.backup.PeriodicBackupOrder
import me.bumiller.ddcmp.domain.model.io.ExportOption
import me.bumiller.ddcmp.domain.usecase.backup.SaveBackupOrderUseCase.I
import me.bumiller.ddcmp.domain.usecase.meta.UseCase
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

class SaveBackupOrderUseCase(
    private val orderRepo: PeriodicBackupOrderRepository
) : UseCase<I, PeriodicBackupOrder> {

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

    override suspend fun invoke(i: I): PeriodicBackupOrder {
        val newOrder = PeriodicBackupOrder(
            id = 0,
            created = Clock.System.now(),
            destination = i.destination,
            active = false,
            period = i.period,
            initialTime = i.initialTime,
            keepLast = i.keepLast,
            exportOption = i.exportOption,
            wasActive = false
        ).let { orderRepo.save(it) }
            .let { orderRepo.id(it).first() ?: error("Didn't find newly created backup order") }

        return newOrder
    }

}