package me.bumiller.ddcmp.work

import dev.brewkits.kmpworkmanager.background.data.IosWorker
import dev.brewkits.kmpworkmanager.background.domain.WorkerEnvironment
import dev.brewkits.kmpworkmanager.background.domain.WorkerResult
import me.bumiller.ddcmp.domain.infrastructure.ExportLogRepository
import me.bumiller.ddcmp.domain.usecase.backup.GetBackupOrderUseCase
import me.bumiller.ddcmp.domain.usecase.backup.PerformBackupOrderUseCase
import me.bumiller.ddcmp.domain.usecase.backup.RecordPeriodicBackupUseCase
import me.bumiller.ddcmp.domain.usecase.io.ExportUseCase
import me.bumiller.ddcmp.domain.usecase.io.GetExportLogUsecase
import me.bumiller.ddcmp.work.worker.periodicBackupWork

internal class PeriodicBackupWorkerIos(
    private val performOrder: PerformBackupOrderUseCase
) : IosWorker {

    override suspend fun doWork(
        input: String?,
        env: WorkerEnvironment
    ): WorkerResult {
        val assertedInput = input ?: error("Input for the PeriodicBackupWorkerIos")

        val errorMsg =
            periodicBackupWork(assertedInput, performOrder)

        return when (errorMsg) {
            null -> WorkerResult.Success()
            else -> WorkerResult.Failure(errorMsg, false)
        }
    }
}