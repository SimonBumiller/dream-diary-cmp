package me.bumiller.ddcomp.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope

actual fun Scope.databaseBuilder(): RoomDatabase.Builder<RelationalDatabase> = Room.databaseBuilder(
    context = androidContext(),
    klass = RelationalDatabase::class.java,
    name = androidContext().getDatabasePath(DB_NAME).absolutePath
)