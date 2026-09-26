package me.ddcmp.feature.settings

import me.ddcmp.feature.settings.entries.root.SettingsRootViewmodel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * The settings koin module.
 */
val settings = module {
    factoryOf(::SettingsRootViewmodel)
}