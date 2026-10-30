package me.ddcmp.feature.io

import me.ddcmp.feature.io.entries.export_configuration.ExportConfigViewModel
import me.ddcmp.feature.io.entries.export_result.ExportResultViewModel
import me.ddcmp.feature.io.entries.import_configuration.ImportConfigViewModel
import me.ddcmp.feature.io.entries.import_result.ImportResultViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * The module for the import feature koin module.
 */
public val import: Module = module {
    factoryOf(::ImportConfigViewModel)
    factoryOf(::ImportResultViewModel)
    factoryOf(::ExportConfigViewModel)
    factoryOf(::ExportResultViewModel)
}