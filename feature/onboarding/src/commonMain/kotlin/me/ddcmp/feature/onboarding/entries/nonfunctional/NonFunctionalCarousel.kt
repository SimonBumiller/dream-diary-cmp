package me.ddcmp.feature.onboarding.entries.nonfunctional

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import dream_diary_cmp.feature.onboarding.generated.resources.Res
import dream_diary_cmp.feature.onboarding.generated.resources.cd_onboarding_connections
import dream_diary_cmp.feature.onboarding.generated.resources.cd_onboarding_data
import dream_diary_cmp.feature.onboarding.generated.resources.cd_onboarding_welcome
import dream_diary_cmp.feature.onboarding.generated.resources.connections_continue_label
import dream_diary_cmp.feature.onboarding.generated.resources.connections_title
import dream_diary_cmp.feature.onboarding.generated.resources.data_description
import dream_diary_cmp.feature.onboarding.generated.resources.data_title
import dream_diary_cmp.feature.onboarding.generated.resources.onboarding_connections
import dream_diary_cmp.feature.onboarding.generated.resources.onboarding_data
import dream_diary_cmp.feature.onboarding.generated.resources.onboarding_welcome
import dream_diary_cmp.feature.onboarding.generated.resources.welcome_description
import dream_diary_cmp.feature.onboarding.generated.resources.welcome_title
import me.ddcmp.feature.onboarding.layout.OnboardingPager
import me.ddcmp.feature.onboarding.layout.SimpleOnboardingLayout
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private data class NonFunctionalScreen(
    val imagePainter: Painter,
    val contentDescription: String,
    val title: String,
    val description: String,
    val continueLabel: String? = null
)

private val screens: List<NonFunctionalScreen>
    @Composable get() = listOf(
        NonFunctionalScreen(
            imagePainter = painterResource(Res.drawable.onboarding_welcome),
            contentDescription = stringResource(Res.string.cd_onboarding_welcome),
            title = stringResource(Res.string.welcome_title),
            description = stringResource(Res.string.welcome_description)
        ),
        NonFunctionalScreen(
            imagePainter = painterResource(Res.drawable.onboarding_data),
            contentDescription = stringResource(Res.string.cd_onboarding_data),
            title = stringResource(Res.string.data_title),
            description = stringResource(Res.string.data_description)
        ),
        NonFunctionalScreen(
            imagePainter = painterResource(Res.drawable.onboarding_connections),
            contentDescription = stringResource(Res.string.cd_onboarding_connections),
            title = stringResource(Res.string.connections_title),
            description = stringResource(Res.string.connections_title),
            continueLabel = stringResource(Res.string.connections_continue_label)
        ),
    )

@Composable
internal fun NonFunctionalCarousel(
    onContinue: () -> Unit
) {
    OnboardingPager(
        modifier = Modifier
            .fillMaxSize(),
        screens = screens
    ) { screen ->
        SimpleOnboardingLayout(
            modifier = Modifier
                .fillMaxSize(),
            imagePainter = screen.imagePainter,
            contentDescription = screen.contentDescription,
            title = screen.title,
            description = screen.description,
            continueLabel = screen.continueLabel,
            onContinue = onContinue
        )
    }
}