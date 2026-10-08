package me.bumiller.ddcmp.domain.usecase.backup

import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.domain.infrastructure.PeriodicBackupRepository
import me.bumiller.ddcmp.domain.model.backup.PeriodicBackup
import me.bumiller.ddcmp.domain.model.io.ExportResult
import me.bumiller.ddcmp.domain.usecase.backup.RecordPeriodicBackupUseCase.I
import me.bumiller.ddcmp.domain.usecase.meta.UseCase
import kotlin.time.Clock

/**
 * Use case used to record that a periodic backup was performed.
 *
 * This should be called in the background, right after a background was performed.
 */
class RecordPeriodicBackupUseCase(
    private val backupRepo: PeriodicBackupRepository,
    private val getOrder: GetBackupOrderUseCase
) : UseCase<I, PeriodicBackup> {

    /**
     * Input for configuring a periodic backup instance.
     */
    data class I(

        /**
         * The order for which it was created.
         */
        val orderId: Long,

        /**
         * The export result.
         */
        val exportResult: ExportResult

    )

    override suspend fun invoke(i: I): PeriodicBackup {
        val now = Clock.System.now()

        val orderQuery = GetBackupOrderUseCase.I(id = i.orderId, active = false)
        val order = getOrder(orderQuery).first()
            ?: error("Illegal order id ${i.orderId} was provided for a backup.")

        val backup = PeriodicBackup(
            id = 0,
            created = now,
            order = order,
            export = i.exportResult
        ).let { backupRepo.save(it) }
            .let { backupRepo.id(it).first()!! }

        return backup
    }

}