package me.bumiller.ddcmp.work

import me.bumiller.ddcmp.work.platform.PlatformBackgroundTaskScheduler
import org.koin.core.module.Module

actual fun org.koin.core.module.Module.providePlatformBackgroundTaskScheduler() {
    single<PlatformBackgroundTaskScheduler> { PlatformBackgroundTaskScheduler() }
}