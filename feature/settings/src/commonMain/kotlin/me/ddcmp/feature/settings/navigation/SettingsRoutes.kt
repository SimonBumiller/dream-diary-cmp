package me.ddcmp.feature.settings.navigation

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface SettingsRoutes {

    /**
     * The root settings page.
     */
    @Serializable
    data object Root: SettingsRoutes

    /**
     * The backup order configuration screen.
     */
    @Serializable
    data object OrderConfig: SettingsRoutes

    /**
     * The screen for viewing orders in detail.
     */
    @Serializable
    data class OrderDetail(val id: Long): SettingsRoutes

}