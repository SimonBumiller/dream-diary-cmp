package me.bumiller.ddcmp.work

import org.koin.core.module.Module

actual fun Module.provideWorkers() {
    single { PeriodicBackupWorkerAndroid(get(), get(), get(), get()) }
}