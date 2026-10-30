package me.bumiller.ddcmp.navigation

import kotlinx.serialization.Serializable

/**
 * The main navigation routes.
 */
@Serializable
internal sealed interface MainRoutes {

    /**
     * Navigation route for the main part of the app.
     */
    @Serializable
    data object Home: MainRoutes

}