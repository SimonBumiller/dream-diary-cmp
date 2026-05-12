package me.ddcmp.feature.home

import me.ddcmp.feature.home.entries.dreamadd.DreamAddViewModel
import me.ddcmp.feature.home.entries.dreamlist.DreamListViewModel
import me.ddcmp.feature.home.entries.emotionlist.EmotionListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * The koin module for the home module.
 */
val home = module {
    viewModelOf(::DreamListViewModel)
    viewModelOf(::DreamAddViewModel)
    viewModelOf(::EmotionListViewModel)
}