package me.ddcmp.feature.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import me.ddcmp.feature.onboarding.entries.functional.FunctionalCarousel
import me.ddcmp.feature.onboarding.entries.nonfunctional.NonFunctionalCarousel
import me.ddcmp.feature.onboarding.navigation.OnboardingRoutes

/**
 * Navigates to the onboarding flow.
 */
public fun NavController.navigateToOnboarding(): Unit =
    navigate(OnboardingRoutes.NonFunctional)

/**
 * Adds the onboarding routes to the navigation graph.
 */
public fun NavGraphBuilder.onboarding(
    onFinished: () -> Unit,
    onImport: () -> Unit,
    navController: NavController
) {
    composable<OnboardingRoutes.NonFunctional> {
        NonFunctionalCarousel(
            onContinue = { navController.navigate(OnboardingRoutes.Functional) }
        )
    }

    composable<OnboardingRoutes.Functional> {
        FunctionalCarousel(
            onContinue = onFinished,
            onImport = onImport
        )
    }
}