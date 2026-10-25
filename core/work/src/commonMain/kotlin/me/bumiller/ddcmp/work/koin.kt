package me.bumiller.ddcmp.work

import me.bumiller.ddcmp.domain.infrastructure.BackupScheduler
import me.bumiller.ddcmp.work.platform.PlatformBackgroundTaskScheduler
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun Module.provideWorkers()
expect fun Module.providePlatformBackgroundTaskScheduler()

val work = module {
    provideWorkers()
    providePlatformBackgroundTaskScheduler()

    singleOf(::PlatformBackupScheduler) bind BackupScheduler::class
}