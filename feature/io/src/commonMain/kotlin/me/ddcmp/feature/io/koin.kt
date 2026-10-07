package me.ddcmp.feature.io

import me.ddcmp.feature.io.entries.import_configuration.ImportConfigViewModel
import me.ddcmp.feature.io.entries.import_result.ImportResultViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * The module for the import feature koin module.
 */
val import = module {
    factoryOf(::ImportConfigViewModel)
    factoryOf(::ImportResultViewModel)
}