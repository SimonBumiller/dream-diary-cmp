package me.ddcmp.feature.stats

import me.ddcmp.feature.stats.entries.DreamGlobalViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * The koin module for the statistics module.
 */
val statistics = module {
    viewModelOf(::DreamGlobalViewModel)
}