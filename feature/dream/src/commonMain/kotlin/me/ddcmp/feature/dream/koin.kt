package me.ddcmp.feature.dream

import me.ddcmp.feature.dream.entries.dreamadd.DreamAddViewModel
import me.ddcmp.feature.dream.entries.dreamdetail.DreamDetailViewModel
import me.ddcmp.feature.dream.entries.dreamlist.DreamListViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * The koin module for the dream module.
 */
public val dream: Module = module {
    factoryOf(::DreamListViewModel)
    factoryOf(::DreamAddViewModel)
    factoryOf(::DreamDetailViewModel)
}