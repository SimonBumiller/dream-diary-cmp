package me.bumiller.ddcmp.navigation

/**
 * The main navigation routes.
 */
internal sealed interface MainRoutes {

    /**
     * Navigation route for the onboarding screen.
     */
    data object Onboarding: MainRoutes

    /**
     * Navigation route for the main part of the app.
     */
    data object Home: MainRoutes

    /**
     * Navigation route for the settings screens.
     */
    data object Settings: MainRoutes

}