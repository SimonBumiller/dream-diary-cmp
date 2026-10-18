package me.bumiller.ddcmp.core.io

import me.bumiller.ddcmp.core.io.factory.ExporterFactoryImpl
import me.bumiller.ddcmp.core.io.factory.ImporterFactoryImpl
import me.bumiller.ddcmp.domain.io.AttachmentStorage
import me.bumiller.ddcmp.domain.io.BackupExporterFactory
import me.bumiller.ddcmp.domain.io.BackupImporterFactory
import me.bumiller.ddcmp.domain.io.DirectoryTruncator
import org.koin.dsl.module

/**
 * Koin module for the io module.
 */
val io = module {
    single<BackupImporterFactory> { ImporterFactoryImpl(get(), get(), get(), get()) }

    single<DirectoryTruncator> { DirectoryTruncatorImpl() }

    single<AttachmentStorage> { AttachmentStorageImpl() }

    single<BackupExporterFactory> { ExporterFactoryImpl(get(), get(), get(), get()) }
}