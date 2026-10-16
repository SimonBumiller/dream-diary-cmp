package me.bumiller.ddcmp.domain.usecase.backup

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.infrastructure.PeriodicBackupRepository
import me.bumiller.ddcmp.domain.model.backup.PeriodicBackup
import me.bumiller.ddcmp.domain.model.io.ExportResult
import me.bumiller.ddcmp.domain.usecase.backup.GetBackupsForOrderUseCase.BackupsForOrder
import me.bumiller.ddcmp.domain.usecase.meta.FlowUseCase

class GetBackupsForOrderUseCase(
    private val backupRepo: PeriodicBackupRepository
): FlowUseCase<Long, BackupsForOrder> {

    override fun invoke(i: Long): Flow<BackupsForOrder> =
        backupRepo.forOrder(i)
            .map(::BackupsForOrder)

    /**
     * Result class of the usecase.
     */
    data class BackupsForOrder(

        /**
         * All backups for the order.
         */
        val backups: List<PeriodicBackup>

    ) {

        /**
         * Total backup size for the order.
         */
        val totalSize: Long
            get() = backups
                .mapNotNull { it.export as? ExportResult.Success }
                .sumOf(ExportResult.Success::fileSize)

        /**
         * The backup that was most recently run.
         */
        val mostRecentBackup: PeriodicBackup?
            get() = backups
                .maxByOrNull(PeriodicBackup::created)

    }

}