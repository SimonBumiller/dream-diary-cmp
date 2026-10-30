package me.ddcmp.feature.settings.navigation

import kotlinx.serialization.Serializable

/**
 * Collects the routes for the settings.
 */
@Serializable
public sealed class SettingsRoutes {

    /**
     * The root settings page.
     */
    @Serializable
    public data object Root: SettingsRoutes()

    /**
     * The backup order configuration screen.
     */
    @Serializable
    internal data object OrderConfig: SettingsRoutes()

    /**
     * The screen for viewing orders in detail.
     */
    @Serializable
    internal data class OrderDetail(val id: Long): SettingsRoutes()

}