package me.ddcmp.feature.onboarding.navigation

import kotlinx.serialization.Serializable

/**
 * Routes for the onboarding.
 */
@Serializable
internal sealed class OnboardingRoutes {

    /**
     * Route that contains the carousel for the non-functional screens.
     */
    @Serializable
    internal data object NonFunctional : OnboardingRoutes()

    /**
     * Route that contains the carousel for the functional screens.
     */
    @Serializable
    internal data object Functional : OnboardingRoutes()

}