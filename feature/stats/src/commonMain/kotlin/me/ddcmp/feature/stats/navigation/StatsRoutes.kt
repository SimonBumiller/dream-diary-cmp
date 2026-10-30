package me.ddcmp.feature.stats.navigation

import kotlinx.serialization.Serializable

/**
 * The navigation routes for the stats section of the app.
 */
@Serializable
public sealed class StatsRoutes {

    /**
     * Route for the global dream stats
     */
    @Serializable
    public data object Global: StatsRoutes()

    /**
     * Route for the specific dream stats.
     */
    @Serializable
    public data class Dream(val id: Long): StatsRoutes()

}