package me.bumiller.ddcmp.work.factory

import dev.brewkits.kmpworkmanager.background.domain.AndroidWorker
import dev.brewkits.kmpworkmanager.background.domain.AndroidWorkerFactory
import me.bumiller.ddcmp.work.PeriodicBackupWorkerAndroid
import org.koin.core.Koin

class AndroidWorkerFactory(private val koin: Koin) : AndroidWorkerFactory {

    override fun createWorker(workerClassName: String): AndroidWorker? = when (workerClassName) {
        "PeriodicBackupWorker" -> koin.get<PeriodicBackupWorkerAndroid>()
        else -> null
    }

}