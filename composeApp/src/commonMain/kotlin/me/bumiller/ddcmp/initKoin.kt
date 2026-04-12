package me.bumiller.ddcmp

import me.bumiller.ddcmp.core.datastore.datastore
import me.bumiller.ddcomp.core.database.database
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

/**
 * Koin initializer.
 *
 * Method called in the initial setup by all platforms.
 */
fun initKoin(config: KoinApplication.() -> Unit = {}) {
    startKoin {
        config()

        modules(database, datastore)
    }
}