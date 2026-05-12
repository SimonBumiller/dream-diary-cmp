package me.bumiller.ddcomp.core.database

import androidx.room.RoomDatabase
import me.bumiller.ddcmp.domain.infrastructure.DreamRepository
import me.bumiller.ddcmp.domain.infrastructure.EmotionRepository
import me.bumiller.ddcmp.domain.infrastructure.PersonRepository
import me.bumiller.ddcomp.core.database.repository.DreamRepository as DreamRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.EmotionRepository as EmotionRepositoryImpl
import me.bumiller.ddcomp.core.database.repository.PersonRepository as PersonRepositoryImpl
import org.koin.core.scope.Scope
import org.koin.dsl.module

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
            .build()
    }

    single { get<RelationalDatabase>().dreamDao() }
    single { get<RelationalDatabase>().emotionDao() }
    single { get<RelationalDatabase>().personDao() }
    single { get<RelationalDatabase>().dreamPersonM2MDao() }
    single { get<RelationalDatabase>().dreamEmotionM2MDao() }

    single<DreamRepository> { DreamRepositoryImpl(get(), get(), get(), get(), get()) }
    single<EmotionRepository> { EmotionRepositoryImpl(get(), get()) }
    single<PersonRepository> { PersonRepositoryImpl(get(), get()) }
}