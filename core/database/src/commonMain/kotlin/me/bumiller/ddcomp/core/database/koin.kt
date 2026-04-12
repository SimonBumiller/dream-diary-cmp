package me.bumiller.ddcomp.core.database

import androidx.room.RoomDatabase
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
}