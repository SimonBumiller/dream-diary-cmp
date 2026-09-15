package me.ddcmp.feature.home

import me.ddcmp.feature.home.entries.dreamadd.DreamAddViewModel
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailViewModel
import me.ddcmp.feature.home.entries.dreamlist.DreamListViewModel
import me.ddcmp.feature.home.entries.emotionadd.EmotionAddViewModel
import me.ddcmp.feature.home.entries.emotionlist.EmotionListViewModel
import me.ddcmp.feature.home.entries.overview.OverviewViewModel
import me.ddcmp.feature.home.entries.personadd.PersonAddViewModel
import me.ddcmp.feature.home.entries.personlist.PersonListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * The koin module for the home module.
 */
val home = module {
    viewModelOf(::DreamListViewModel)
    viewModelOf(::DreamAddViewModel)
    viewModelOf(::EmotionListViewModel)
    viewModelOf(::EmotionAddViewModel)
    viewModelOf(::PersonListViewModel)
    viewModelOf(::PersonAddViewModel)
    viewModelOf(::DreamDetailViewModel)
    viewModelOf(::OverviewViewModel)
}