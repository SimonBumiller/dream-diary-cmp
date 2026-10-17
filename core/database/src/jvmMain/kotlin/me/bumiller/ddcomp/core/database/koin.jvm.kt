package me.bumiller.ddcomp.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.vinceglb.filekit.PlatformFile
import me.bumiller.ddcmp.common.dataDirectory
import org.koin.core.scope.Scope
import java.io.File

actual fun Scope.databaseBuilder(): RoomDatabase.Builder<RelationalDatabase> {
    val dbFile = PlatformFile(dataDirectory(), DB_NAME).file
    return Room.databaseBuilder(dbFile.absolutePath)
}