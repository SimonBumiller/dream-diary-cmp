package me.ddcmp.feature.onboarding

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import me.ddcmp.feature.onboarding.entries.functional.FunctionalCarousel
import me.ddcmp.feature.onboarding.entries.nonfunctional.NonFunctionalCarousel
import me.ddcmp.feature.onboarding.navigation.OnboardingRoutes

@Composable
fun OnboardingEntry(
    onFinished: () -> Unit
) {
    val backstack =
        remember { mutableStateListOf<OnboardingRoutes>(OnboardingRoutes.NonFunctional) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        NavDisplay(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
            backStack = backstack,
            onBack = { backstack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<OnboardingRoutes.NonFunctional> {
                    NonFunctionalCarousel(
                        onContinue = { backstack.add(OnboardingRoutes.Functional) }
                    )
                }

                entry<OnboardingRoutes.Functional> {
                    FunctionalCarousel(onFinished)
                }
            }
        )
    }
}