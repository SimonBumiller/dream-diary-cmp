package me.bumiller.ddcmp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.github.vinceglb.filekit.PlatformFile
import me.bumiller.ddcmp.common.dataDirectory
import okio.Path.Companion.toOkioPath
import org.koin.core.scope.Scope
import java.nio.file.Path

private const val DatastoreDirectoryName = "datastore"

actual fun Scope.instantiateDataStore(): DataStore<Preferences> {
    val path = PlatformFile(dataDirectory(), DatastoreDirectoryName).file.toOkioPath()
    return createDataStore(path)
}