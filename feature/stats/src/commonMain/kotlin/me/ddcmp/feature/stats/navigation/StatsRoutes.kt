package me.ddcmp.feature.stats.navigation

import kotlinx.serialization.Serializable

/**
 * The navigation routes for the stats section of the app.
 */
@Serializable
internal sealed interface StatsRoutes {

    /**
     * Route for the global dream stats
     */
    @Serializable
    data object DreamGlobal: StatsRoutes

}