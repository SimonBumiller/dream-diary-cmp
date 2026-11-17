package me.ddcmp.feature.emotion

import me.ddcmp.feature.emotion.entries.emotionadd.EmotionAddViewModel
import me.ddcmp.feature.emotion.entries.emotionlist.EmotionListViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * The koin module for the emotion module.
 */
public val emotion: Module = module {
    factoryOf(::EmotionListViewModel)
    factoryOf(::EmotionAddViewModel)
}