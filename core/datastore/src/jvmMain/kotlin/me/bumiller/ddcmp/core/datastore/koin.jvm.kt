package me.bumiller.ddcmp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toOkioPath
import org.koin.core.scope.Scope
import java.nio.file.Path

actual fun Scope.instantiateDataStore(): DataStore<Preferences> {
    val path = Path.of(System.getProperty("java.io.tmpdir")).toOkioPath()
    return createDataStore(path)
}