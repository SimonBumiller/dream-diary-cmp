package me.bumiller.ddcomp.core.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import me.bumiller.ddcmp.domain.infrastructure.DreamRepository
import me.bumiller.ddcmp.domain.infrastructure.EmotionRepository
import me.bumiller.ddcmp.domain.infrastructure.ExportLogRepository
import me.bumiller.ddcmp.domain.infrastructure.ImportLogRepository
import me.bumiller.ddcmp.domain.infrastructure.PeriodicBackupOrderRepository
import me.bumiller.ddcmp.domain.infrastructure.PeriodicBackupRepository
import me.bumiller.ddcmp.domain.infrastructure.PersonRepository
import me.bumiller.ddcmp.domain.infrastructure.TagRepository
import me.bumiller.ddcmp.domain.infrastructure.AttachmentMetaRepository
import org.koin.core.scope.Scope
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import me.bumiller.ddcomp.core.database.repository.DreamRepository as DreamRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.EmotionRepository as EmotionRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.ImportLogRepository as ImportLogRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.ExportLogRepository as ExportLogRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.PersonRepository as PersonRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.PeriodicBackupOrderRepository as PeriodicBackupOrderRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.PeriodicBackupRepository as PeriodicBackupRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.TagRepository as TagRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.AttachmentMetaRepository as AttachmentMetaRepositoryImpl

/**
 * Platform specific method to create the database builder
 */
expect fun Scope.databaseBuilder(): RoomDatabase.Builder<RelationalDatabase>

/**
 * Database koin module.
 */
val database = module {
    single {
        databaseBuilder()
            .fallbackToDestructiveMigration(true)
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single { get<RelationalDatabase>().dreamDao() }
    single { get<RelationalDatabase>().emotionDao() }
    single { get<RelationalDatabase>().personDao() }
    single { get<RelationalDatabase>().dreamPersonM2MDao() }
    single { get<RelationalDatabase>().dreamEmotionM2MDao() }
    single { get<RelationalDatabase>().importLogDao() }
    single { get<RelationalDatabase>().exportLogDao() }
    single { get<RelationalDatabase>().periodicBackupOrderDao() }
    single { get<RelationalDatabase>().periodicBackupDao() }
    single { get<RelationalDatabase>().backupDestinationDao() }
    single { get<RelationalDatabase>().dreamTagM2MDao() }
    single { get<RelationalDatabase>().personTagM2MDao() }
    single { get<RelationalDatabase>().tagDao() }
    single { get<RelationalDatabase>().attachmentMetaDao() }

    singleOf(::DreamRepositoryImpl) bind DreamRepository::class
    singleOf(::EmotionRepositoryImpl) bind EmotionRepository::class
    singleOf(::PersonRepositoryImpl) bind PersonRepository::class
    singleOf(::ImportLogRepositoryImpl) bind ImportLogRepository::class
    singleOf(::ExportLogRepositoryImpl) bind ExportLogRepository::class
    singleOf(::PeriodicBackupOrderRepositoryImpl) bind PeriodicBackupOrderRepository::class
    singleOf(::PeriodicBackupRepositoryImpl) bind PeriodicBackupRepository::class
    singleOf(::TagRepositoryImpl) bind TagRepository::class
    singleOf(::AttachmentMetaRepositoryImpl) bind AttachmentMetaRepository::class
}