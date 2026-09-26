package me.ddcmp.feature.stats

import me.ddcmp.feature.stats.entries.dream.DreamViewModel
import me.ddcmp.feature.stats.entries.dreamglobal.DreamGlobalViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * The koin module for the statistics module.
 */
val statistics = module {
    factoryOf(::DreamGlobalViewModel)
    factoryOf(::DreamViewModel)
}