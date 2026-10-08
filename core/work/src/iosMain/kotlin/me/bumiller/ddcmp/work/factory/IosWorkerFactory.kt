package me.bumiller.ddcmp.work.factory

import dev.brewkits.kmpworkmanager.background.data.IosWorker
import dev.brewkits.kmpworkmanager.background.data.IosWorkerFactory
import me.bumiller.ddcmp.work.worker.PeriodicBackupWorkerIos
import org.koin.core.Koin

class IosWorkerFactory(private val koin: Koin) : IosWorkerFactory {

    override fun createWorker(workerClassName: String): IosWorker? = when (workerClassName) {
        "PeriodicBackupWorker" -> koin.get<PeriodicBackupWorkerIos>()
        else -> null
    }

}