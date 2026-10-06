package me.bumiller.ddcomp.core.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import me.bumiller.ddcmp.domain.infrastructure.DreamRepository
import me.bumiller.ddcmp.domain.infrastructure.EmotionRepository
import me.bumiller.ddcmp.domain.infrastructure.ExportLogRepository
import me.bumiller.ddcmp.domain.infrastructure.ImportLogRepository
import me.bumiller.ddcmp.domain.infrastructure.PersonRepository
import org.koin.core.scope.Scope
import org.koin.dsl.module
import me.bumiller.ddcomp.core.database.repository.DreamRepository as DreamRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.EmotionRepository as EmotionRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.ImportLogRepository as ImportLogRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.ExportLogRepository as ExportLogRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.PersonRepository as PersonRepositoryImpl

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

    single<DreamRepository> { DreamRepositoryImpl(get(), get(), get()) }
    single<EmotionRepository> { EmotionRepositoryImpl(get(), get()) }
    single<PersonRepository> { PersonRepositoryImpl(get(), get()) }
    single<ImportLogRepository> { ImportLogRepositoryImpl(get()) }
    single<ExportLogRepository> { ExportLogRepositoryImpl(get()) }
}