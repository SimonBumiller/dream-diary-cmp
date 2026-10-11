package me.bumiller.ddcmp.work

import dev.brewkits.kmpworkmanager.KmpWorkManager
import dev.brewkits.kmpworkmanager.background.domain.BackgroundTaskScheduler
import me.bumiller.ddcmp.work.platform.PlatformBackgroundTaskScheduler
import org.koin.core.module.Module

actual fun Module.provideWorkers() {
    single { PeriodicBackupWorkerAndroid(get(), get(), get(), get()) }
}

actual fun Module.providePlatformBackgroundTaskScheduler() {
    single<BackgroundTaskScheduler> { KmpWorkManager.getInstance().backgroundTaskScheduler }
    single<PlatformBackgroundTaskScheduler> { PlatformBackgroundTaskScheduler(get()) }
}