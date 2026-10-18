package me.bumiller.ddcmp.work

import dev.brewkits.kmpworkmanager.background.domain.AndroidWorker
import dev.brewkits.kmpworkmanager.background.domain.WorkerEnvironment
import dev.brewkits.kmpworkmanager.background.domain.WorkerResult
import me.bumiller.ddcmp.domain.infrastructure.ExportLogRepository
import me.bumiller.ddcmp.domain.usecase.backup.GetBackupOrderUseCase
import me.bumiller.ddcmp.domain.usecase.backup.PerformBackupOrderUseCase
import me.bumiller.ddcmp.domain.usecase.backup.RecordPeriodicBackupUseCase
import me.bumiller.ddcmp.domain.usecase.io.ExportUseCase
import me.bumiller.ddcmp.domain.usecase.io.GetExportLogUsecase
import me.bumiller.ddcmp.work.worker.periodicBackupWork

internal class PeriodicBackupWorkerAndroid(
    private val performOrder: PerformBackupOrderUseCase
) : AndroidWorker {

    override suspend fun doWork(
        input: String?,
        env: WorkerEnvironment
    ): WorkerResult {
        val assertedInput = input ?: error("Input for the PeriodicBackupWorkerAndroid")

        val errorMsg =
            periodicBackupWork(assertedInput, performOrder)

        return when (errorMsg) {
            null -> WorkerResult.Success()
            else -> WorkerResult.Failure(errorMsg, false)
        }
    }
}