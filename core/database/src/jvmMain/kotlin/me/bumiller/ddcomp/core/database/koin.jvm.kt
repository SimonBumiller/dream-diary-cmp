package me.bumiller.ddcomp.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.scope.Scope
import java.io.File

actual fun Scope.databaseBuilder(): RoomDatabase.Builder<RelationalDatabase> {
    // TODO: Change path assignment
    val dbFile = File(System.getProperty("java.io.tmpdir"), DB_NAME)
    return Room.databaseBuilder(dbFile.absolutePath)
}