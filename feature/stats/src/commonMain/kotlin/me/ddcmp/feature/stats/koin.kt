package me.ddcmp.feature.stats

import me.ddcmp.feature.stats.entries.dream.DreamViewModel
import me.ddcmp.feature.stats.entries.dreamglobal.DreamGlobalViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * The koin module for the statistics module.
 */
val statistics = module {
    viewModelOf(::DreamGlobalViewModel)
    viewModelOf(::DreamViewModel)
}