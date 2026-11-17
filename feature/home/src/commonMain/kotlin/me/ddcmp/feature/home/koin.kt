package me.ddcmp.feature.home

import me.ddcmp.feature.home.entries.overview.OverviewViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * The koin module for the home module.
 */
public val home: Module = module {
    factoryOf(::OverviewViewModel)
}