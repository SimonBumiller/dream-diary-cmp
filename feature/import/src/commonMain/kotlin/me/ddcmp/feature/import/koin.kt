package me.ddcmp.feature.import

import me.ddcmp.feature.import.entries.configuration.ConfigurationViewModel
import me.ddcmp.feature.import.entries.result.ResultViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * The module for the import feature koin module.
 */
val import = module {
    factoryOf(::ConfigurationViewModel)
    factoryOf(::ResultViewModel)
}