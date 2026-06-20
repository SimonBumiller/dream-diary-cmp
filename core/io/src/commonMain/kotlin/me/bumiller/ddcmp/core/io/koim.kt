package me.bumiller.ddcmp.core.io

import me.bumiller.ddcmp.domain.io.BackupImporterFactory
import org.koin.dsl.module

/**
 * Koin module for the io module.
 */
val io = module {
    single<BackupImporterFactory> { ImporterFactoryImpl(get(), get(), get()) }
}