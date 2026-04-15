package me.ddcmp.feature.onboarding

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

/**
 * Styles for the onboarding section.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
internal object OnboardingStyles {

    /**
     * Style for the title of a onboarding screen.
     */
    val screenTitle: TextStyle
        @Composable get() = MaterialTheme.typography.displaySmallEmphasized
            .copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )

    /**
     * Style for the description of a onboarding screen.
     */
    val screenDescription: TextStyle
        @Composable get() = MaterialTheme.typography.bodyLarge
            .copy(textAlign = TextAlign.Center)

    /**
     * Color for the inactive dots of the page indicator.
     */
    val pagerIndicatorInactive: Color
        @Composable get() = MaterialTheme.colorScheme.outline
            .copy(alpha = 0.38F)

    /**
     * Style for the name of a confidentiality item.
     */
    val confidentialityItemTitle: TextStyle
         @Composable get() = MaterialTheme.typography.titleMedium
             .copy(fontWeight = FontWeight.Bold)

}