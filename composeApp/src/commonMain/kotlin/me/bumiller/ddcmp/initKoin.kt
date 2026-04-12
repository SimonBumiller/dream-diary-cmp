package me.bumiller.ddcmp

import me.bumiller.ddcmp.core.datastore.datastore
import me.bumiller.ddcmp.domain.domain
import me.bumiller.ddcomp.core.database.database
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

/**
 * The koin module for the app.
 */
private val app = module {
    viewModelOf(::AppViewmodel)
}

/**
 * Koin initializer.
 *
 * Method called in the initial setup by all platforms.
 */
fun initKoin(config: KoinApplication.() -> Unit = {}) {
    startKoin {
        config()

        modules(app, database, datastore, domain)
    }
}