package me.bumiller.ddcmp.work

import me.bumiller.ddcmp.work.platform.PlatformBackgroundTaskScheduler
import org.koin.core.module.Module


actual fun Module.providePlatformBackgroundTaskScheduler() {
    single<PlatformBackgroundTaskScheduler> { PlatformBackgroundTaskScheduler() }
}

actual fun Module.provideWorkers() {
    // No workers on jvm
}