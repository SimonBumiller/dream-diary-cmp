package me.bumiller.ddcmp.domain.usecase.backup

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.exists
import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.common.createChildFile
import me.bumiller.ddcmp.domain.infrastructure.ExportLogRepository
import me.bumiller.ddcmp.domain.io.DirectoryTruncator
import me.bumiller.ddcmp.domain.model.backup.BackupDestination
import me.bumiller.ddcmp.domain.model.backup.PeriodicBackupOrder
import me.bumiller.ddcmp.domain.model.io.ExportResult
import me.bumiller.ddcmp.domain.usecase.backup.PerformBackupOrderUseCase.I
import me.bumiller.ddcmp.domain.usecase.io.ExportUseCase
import me.bumiller.ddcmp.domain.usecase.io.GetExportLogUsecase
import me.bumiller.ddcmp.domain.usecase.meta.UseCase
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * Usecase that performs a backup for a periodic backup order.
 *
 * This handles the work, not the scheduling.
 */
class PerformBackupOrderUseCase(
    private val getOrder: GetBackupOrderUseCase,
    private val getExportLog: GetExportLogUsecase,
    private val recordBackup: RecordPeriodicBackupUseCase,
    private val export: ExportUseCase,
    private val exportLogRepo: ExportLogRepository,
    private val directoryTruncator: DirectoryTruncator
): UseCase<I, Boolean> {

    override suspend fun invoke(i: I): Boolean {
        val startTimestamp = Clock.System.now()

        // Getting the order for which the backup is created.
        val orderQuery = GetBackupOrderUseCase.I(id = i.backupOrderId, active = false)
        val order = getOrder(orderQuery).first()
            ?: error("Order with id ${i.backupOrderId} was not found for ongoing backup.")

        // Creating the backup and getting the export result.
        val exportResultId = createExportResult(order, startTimestamp)
        val exportResult = getExportLog(exportResultId) ?: error("Export log for just finished export not found.")

        if(order.keepLast != null) {
            directoryTruncator.ensureMaxFileCount(order.destinationAsFile(), order.keepLast)
        }

        // Recording the backup.
        val backupRecord = RecordPeriodicBackupUseCase.I(
            orderId = order.id,
            exportResult = exportResult
        )
        recordBackup(backupRecord)

        // Backup is exactly then successful if the export was successful.
        return exportResult is ExportResult.Success
    }

    private suspend fun createExportResult(order: PeriodicBackupOrder, started: Instant): Long {
        val backupDirectory = order.destinationAsFile()
        val backupFileName = order.createBackupFileName(Clock.System.now())

        if(backupDirectory.exists()) {
            val backupOutputFile = order.destinationAsFile().createChildFile(backupFileName)

            // Reconstructing the export options for the export.
            val exportConfig = ExportUseCase.I(
                destination = backupOutputFile,
                options = order.exportOption
            )

            return export(exportConfig)
        } else { // If directory doesn't exist, we create an export error manually.
            val result = ExportResult.Failure.FileAccess(
                id = 0,
                started = started,
                ended = Clock.System.now()
            )

            return exportLogRepo.save(result)
        }
    }

    private fun PeriodicBackupOrder.destinationAsFile(): PlatformFile = when (destination) {
        is BackupDestination.LocalDirectory -> destination.directory
    }

    /**
     * Input to the usecase.
     */
    data class I(

        /**
         * The id of the order for which the backup is performed.
         */
        val backupOrderId: Long
    )

}