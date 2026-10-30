package me.ddcmp.feature.settings

import me.ddcmp.feature.settings.entries.order_config.OrderConfigViewModel
import me.ddcmp.feature.settings.entries.order_detail.OrderDetailViewModel
import me.ddcmp.feature.settings.entries.root.SettingsRootViewmodel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * The settings koin module.
 */
public val settings: Module = module {
    factoryOf(::SettingsRootViewmodel)
    factoryOf(::OrderConfigViewModel)
    factoryOf(::OrderDetailViewModel)
}