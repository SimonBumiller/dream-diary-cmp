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
import org.koin.core.scope.Scope
import org.koin.dsl.module
import me.bumiller.ddcomp.core.database.repository.DreamRepository as DreamRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.EmotionRepository as EmotionRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.ImportLogRepository as ImportLogRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.ExportLogRepository as ExportLogRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.PersonRepository as PersonRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.PeriodicBackupOrderRepository as PeriodicBackupOrderRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.PeriodicBackupRepository as PeriodicBackupRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.TagRepository as TagRepositoryImpl

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

    single<DreamRepository> { DreamRepositoryImpl(get(), get(), get(), get(), get(), get()) }
    single<EmotionRepository> { EmotionRepositoryImpl(get(), get()) }
    single<PersonRepository> { PersonRepositoryImpl(get(), get(), get()) }
    single<ImportLogRepository> { ImportLogRepositoryImpl(get()) }
    single<ExportLogRepository> { ExportLogRepositoryImpl(get()) }
    single<PeriodicBackupOrderRepository> { PeriodicBackupOrderRepositoryImpl(get(), get()) }
    single<PeriodicBackupRepository> { PeriodicBackupRepositoryImpl(get(), get()) }
    single<TagRepository> { TagRepositoryImpl(get(), get(), get(), get(), get()) }
}