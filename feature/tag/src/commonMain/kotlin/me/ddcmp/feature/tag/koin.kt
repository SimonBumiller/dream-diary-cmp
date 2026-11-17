package me.ddcmp.feature.tag

import me.ddcmp.feature.tag.entries.tagadd.TagAddViewModel
import me.ddcmp.feature.tag.entries.taglist.TagListViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * The koin module for the tag module.
 */
public val home: Module = module {
    factoryOf(::TagListViewModel)
    factoryOf(::TagAddViewModel)
}