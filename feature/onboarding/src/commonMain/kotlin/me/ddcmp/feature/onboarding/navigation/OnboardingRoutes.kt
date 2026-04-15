package me.ddcmp.feature.onboarding.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Routes for the onboarding.
 */
internal sealed interface OnboardingRoutes : NavKey {

    /**
     * Route that contains the carousel for the non-functional screens.
     */
    data object NonFunctional : OnboardingRoutes

    /**
     * Route that contains the carousel for the functional screens.
     */
    data object Functional : OnboardingRoutes

}