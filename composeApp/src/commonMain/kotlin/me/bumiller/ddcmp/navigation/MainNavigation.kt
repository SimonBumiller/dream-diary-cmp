package me.bumiller.ddcmp.navigation

import kotlinx.serialization.Serializable

/**
 * The main navigation routes.
 */
@Serializable
internal sealed interface MainRoutes {

    /**
     * Navigation route for the onboarding screen.
     */
    @Serializable
    data object Onboarding: MainRoutes

    /**
     * Navigation route for the main part of the app.
     */
    @Serializable
    data object Home: MainRoutes

    /**
     * Navigation route for the import section of the app.
     */
    @Serializable
    data object Import: MainRoutes

    /**
     * Navigation route for the export section of the app.
     */
    @Serializable
    data object Export: MainRoutes

}