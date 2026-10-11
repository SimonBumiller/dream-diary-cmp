package me.bumiller.ddcmp.work.worker

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absoluteFile
import io.github.vinceglb.filekit.createDirectories
import io.github.vinceglb.filekit.div
import io.github.vinceglb.filekit.exists
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import me.bumiller.ddcmp.domain.infrastructure.ExportLogRepository
import me.bumiller.ddcmp.domain.io.createChildFile
import me.bumiller.ddcmp.domain.model.backup.BackupDestination
import me.bumiller.ddcmp.domain.model.backup.PeriodicBackupOrder
import me.bumiller.ddcmp.domain.model.io.ExportResult
import me.bumiller.ddcmp.domain.usecase.backup.GetBackupOrderUseCase
import me.bumiller.ddcmp.domain.usecase.backup.RecordPeriodicBackupUseCase
import me.bumiller.ddcmp.domain.usecase.io.ExportUseCase
import me.bumiller.ddcmp.domain.usecase.io.GetExportLogUsecase
import kotlin.time.Clock

@Serializable
internal data class PeriodicBackupWorkInput(

    val backupOrderId: Long

)

/**
 * Performs the work for the periodic backup.
 *
 * Uses several domain use-cases.
 *
 * @param inputString The input, adhering to [PeriodicBackupWorkInput]
 * @return The error message, or null for success
 */
internal suspend fun periodicBackupWork(
    inputString: String,
    getOrder: GetBackupOrderUseCase,
    getExportLog: GetExportLogUsecase,
    recordBackup: RecordPeriodicBackupUseCase,
    export: ExportUseCase,
    exportLogRepo: ExportLogRepository
): String? {
    val input = Json.decodeFromString<PeriodicBackupWorkInput>(inputString)
    val query = GetBackupOrderUseCase.I(id = input.backupOrderId, active = false)

    // The order for which the current backup is processing.
    val order = getOrder(query).first()
        ?: error("Order with id ${input.backupOrderId} was not found for ongoing backup.")

    val backupFileName = order.createBackupFileName(Clock.System.now())
    val backupDirectory = order.destinationAsFile()

    val exportResultId = if(!backupDirectory.exists()) {
        ExportResult.Failure.FileAccess(
            id = 0,
            timestamp = Clock.System.now()
        ).let { exportLogRepo.save(it) }
    } else {
        val backupOutputFile = order.destinationAsFile().createChildFile(backupFileName)

        // Reconstructing the export options for the export.
        val exportConfig = ExportUseCase.I(
            destination = backupOutputFile,
            options = order.exportOption
        )

        export(exportConfig)

    }
    val exportResult = getExportLog(exportResultId) ?: error("Export log for just finished export not found.")

    RecordPeriodicBackupUseCase.I(
        orderId = order.id,
        exportResult = exportResult
    ).let { recordBackup(it) }

    return when (exportResult) {
        is ExportResult.Failure.FileAccess -> "A problem occurred trying to write the backup file"
        is ExportResult.Success -> null
    }
}

private fun PeriodicBackupOrder.destinationAsFile(): PlatformFile = when (destination) {
    is BackupDestination.LocalDirectory -> (destination as BackupDestination.LocalDirectory).directory
}