package me.bumiller.ddcmp.work.worker

import dev.brewkits.kmpworkmanager.background.data.IosWorker
import dev.brewkits.kmpworkmanager.background.domain.WorkerEnvironment
import dev.brewkits.kmpworkmanager.background.domain.WorkerResult
import me.bumiller.ddcmp.domain.infrastructure.ExportLogRepository
import me.bumiller.ddcmp.domain.usecase.backup.GetBackupOrderUseCase
import me.bumiller.ddcmp.domain.usecase.backup.PerformBackupOrderUseCase
import me.bumiller.ddcmp.domain.usecase.backup.RecordPeriodicBackupUseCase
import me.bumiller.ddcmp.domain.usecase.io.ExportUseCase
import me.bumiller.ddcmp.domain.usecase.io.GetExportLogUsecase

internal class PeriodicBackupWorkerIos(
    private val performBackup: PerformBackupOrderUseCase
) : IosWorker {

    override suspend fun doWork(
        input: String?,
        env: WorkerEnvironment
    ): WorkerResult {
        val assertedInput = input ?: error("Input for the PeriodicBackupWorkerAndroid")

        val errorMsg =
            periodicBackupWork(assertedInput, performBackup)

        return when (errorMsg) {
            null -> WorkerResult.Success()
            else -> WorkerResult.Failure(errorMsg, false)
        }
    }
}