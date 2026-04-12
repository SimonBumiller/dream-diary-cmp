package me.bumiller.ddcomp.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.scope.Scope
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun Scope.databaseBuilder(): RoomDatabase.Builder<RelationalDatabase> {
    val fileManager = NSFileManager.defaultManager
    val urls = fileManager.URLsForDirectory(
        directory = NSApplicationSupportDirectory,
        inDomains = NSUserDomainMask
    )

    val appSupportUrl = urls.first() as NSURL

    val dbUrl = appSupportUrl.URLByAppendingPathComponent("my-database.db")!!

    // Ensure directory exists
    fileManager.createDirectoryAtURL(
        appSupportUrl,
        withIntermediateDirectories = true,
        attributes = null,
        error = null
    )

    return Room.databaseBuilder(dbUrl.absoluteString ?: throw IllegalStateException())
}