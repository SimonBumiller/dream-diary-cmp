package me.bumiller.ddcmp.work

import org.koin.core.module.Module

actual fun Module.provideWorkers() {
    single { PeriodicBackupWorkerIos(get(), get(), get(), get()) }
}