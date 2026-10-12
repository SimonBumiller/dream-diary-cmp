package me.bumiller.ddcmp.core.io

import me.bumiller.ddcmp.core.io.factory.ExporterFactoryImpl
import me.bumiller.ddcmp.core.io.factory.ImporterFactoryImpl
import me.bumiller.ddcmp.domain.io.BackupExporterFactory
import me.bumiller.ddcmp.domain.io.BackupImporterFactory
import org.koin.dsl.module

/**
 * Koin module for the io module.
 */
val io = module {
    single<BackupImporterFactory> { ImporterFactoryImpl(get(), get(), get(), get()) }

    single<BackupExporterFactory> { ExporterFactoryImpl(get(), get(), get(), get()) }
}