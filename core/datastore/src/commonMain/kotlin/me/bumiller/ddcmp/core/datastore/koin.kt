package me.bumiller.ddcmp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import me.bumiller.ddcmp.domain.infrastructure.SettingsStore
import okio.Path
import org.koin.core.scope.Scope
import org.koin.dsl.module

private const val FILENAME = "settings.datastore"

/**
 * Creates a datastore inside a given parent directory.
 */
fun createDataStore(dir: Path) =
    PreferenceDataStoreFactory.createWithPath { dir.resolve(FILENAME) }

expect fun Scope.instantiateDataStore(): DataStore<Preferences>

/**
 * The Koin module for the datastore component.
 */
val datastore = module {
    single<DataStore<Preferences>> { instantiateDataStore() }

    single<SettingsStore> { DataStoreSettingsStore(get()) }
}