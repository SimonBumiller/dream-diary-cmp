package me.bumiller.ddcmp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toOkioPath
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope

internal actual fun Scope.instantiateDataStore(): DataStore<Preferences> =
    createDataStore(androidContext().filesDir.toOkioPath())