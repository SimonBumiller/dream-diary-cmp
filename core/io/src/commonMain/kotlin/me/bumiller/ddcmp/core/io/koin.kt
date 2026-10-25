package me.bumiller.ddcmp.core.io

import me.bumiller.ddcmp.core.io.factory.ExporterFactoryImpl
import me.bumiller.ddcmp.core.io.factory.ImporterFactoryImpl
import me.bumiller.ddcmp.domain.io.AttachmentStorage
import me.bumiller.ddcmp.domain.io.BackupExporterFactory
import me.bumiller.ddcmp.domain.io.BackupImporterFactory
import me.bumiller.ddcmp.domain.io.DirectoryTruncator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Koin module for the io module.
 */
val io = module {
    singleOf(::ImporterFactoryImpl) bind BackupImporterFactory::class

    singleOf(::DirectoryTruncatorImpl) bind DirectoryTruncator::class

    singleOf(::AttachmentStorageImpl) bind AttachmentStorage::class

    singleOf(::ExporterFactoryImpl) bind BackupExporterFactory::class
}