package me.ddcmp.feature.home

import me.ddcmp.feature.home.entries.dreamadd.DreamAddViewModel
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailViewModel
import me.ddcmp.feature.home.entries.dreamlist.DreamListViewModel
import me.ddcmp.feature.home.entries.emotionadd.EmotionAddViewModel
import me.ddcmp.feature.home.entries.emotionlist.EmotionListViewModel
import me.ddcmp.feature.home.entries.overview.OverviewViewModel
import me.ddcmp.feature.home.entries.personadd.PersonAddViewModel
import me.ddcmp.feature.home.entries.persondetail.PersonDetailViewModel
import me.ddcmp.feature.home.entries.personlist.PersonListViewModel
import me.ddcmp.feature.home.entries.tagadd.TagAddViewModel
import me.ddcmp.feature.home.entries.taglist.TagListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * The koin module for the home module.
 */
val home = module {
    factoryOf(::DreamListViewModel)
    factoryOf(::DreamAddViewModel)
    factoryOf(::EmotionListViewModel)
    factoryOf(::EmotionAddViewModel)
    factoryOf(::PersonListViewModel)
    factoryOf(::PersonAddViewModel)
    factoryOf(::DreamDetailViewModel)
    factoryOf(::OverviewViewModel)
    factoryOf(::PersonDetailViewModel)
    factoryOf(::TagListViewModel)
    factoryOf(::TagAddViewModel)
}