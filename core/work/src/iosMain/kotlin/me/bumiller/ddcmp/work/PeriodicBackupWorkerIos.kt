package me.bumiller.ddcmp.work

import dev.brewkits.kmpworkmanager.background.data.IosWorker
import dev.brewkits.kmpworkmanager.background.domain.WorkerEnvironment
import dev.brewkits.kmpworkmanager.background.domain.WorkerResult
import me.bumiller.ddcmp.domain.usecase.backup.GetBackupOrderUseCase
import me.bumiller.ddcmp.domain.usecase.backup.RecordPeriodicBackupUseCase
import me.bumiller.ddcmp.domain.usecase.io.ExportUseCase
import me.bumiller.ddcmp.domain.usecase.io.GetExportLogUsecase
import me.bumiller.ddcmp.work.worker.periodicBackupWork

internal class PeriodicBackupWorkerIos(
    private val getOrder: GetBackupOrderUseCase,
    private val getExportLog: GetExportLogUsecase,
    private val recordBackup: RecordPeriodicBackupUseCase,
    private val export: ExportUseCase
) : IosWorker {

    override suspend fun doWork(
        input: String?,
        env: WorkerEnvironment
    ): WorkerResult {
        val assertedInput = input ?: error("Input for the PeriodicBackupWorkerAndroid")

        val errorMsg =
            periodicBackupWork(assertedInput, getOrder, getExportLog, recordBackup, export)

        return when (errorMsg) {
            null -> WorkerResult.Success()
            else -> WorkerResult.Failure(errorMsg, false)
        }
    }
}