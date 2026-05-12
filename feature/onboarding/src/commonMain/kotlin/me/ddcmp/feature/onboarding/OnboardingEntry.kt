package me.ddcmp.feature.onboarding

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.ddcmp.feature.onboarding.entries.functional.FunctionalCarousel
import me.ddcmp.feature.onboarding.entries.nonfunctional.NonFunctionalCarousel
import me.ddcmp.feature.onboarding.navigation.OnboardingRoutes

@Composable
fun OnboardingEntry(
    onFinished: () -> Unit
) {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
            navController = navController,
            startDestination = OnboardingRoutes.NonFunctional
        ) {
            composable<OnboardingRoutes.NonFunctional> {
                NonFunctionalCarousel(
                    onContinue = { navController.navigate(OnboardingRoutes.Functional) }
                )
            }

            composable<OnboardingRoutes.Functional> {
                FunctionalCarousel(onFinished)
            }
        }
    }
}