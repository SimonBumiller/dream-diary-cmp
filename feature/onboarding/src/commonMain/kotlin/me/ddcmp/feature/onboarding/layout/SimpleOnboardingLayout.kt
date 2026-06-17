package me.ddcmp.feature.onboarding.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import me.bumiller.ddcmp.ui.common.LocalWindowSizeClass
import me.ddcmp.feature.onboarding.OnboardingStyles

internal val WindowSizeClass.doVertical: Boolean
    get() = when (this.widthSizeClass) {
        WindowWidthSizeClass.Compact -> true
        WindowWidthSizeClass.Medium -> when (this.heightSizeClass) {
            WindowHeightSizeClass.Compact -> false
            else -> true
        }

        else -> false
    }

/**
 * The base layout for an onboarding screen.
 */
@Composable
internal fun BaseOnboardingLayout(
    modifier: Modifier,
    primaryPadding: Boolean = true,
    verticalPrimaryWeight: Float = 0.66F,
    horizontalPrimaryWeight: Float = 0.5F,
    primary: @Composable () -> Unit,
    secondary: @Composable ColumnScope.() -> Unit
) {
    val sizeClass = LocalWindowSizeClass.current

    val padding = if(primaryPadding) PaddingValues(horizontal = 48.dp)
        else PaddingValues()

    if (sizeClass.doVertical) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(verticalPrimaryWeight)
                    .fillMaxWidth()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                primary()
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F - verticalPrimaryWeight),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                secondary()
            }

        }
    } else {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(horizontalPrimaryWeight)
                    .fillMaxWidth()
                    .padding(48.dp),
                contentAlignment = Alignment.Center
            ) {
                primary()
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1 - horizontalPrimaryWeight),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                secondary()
            }

        }
    }
}

/**
 * Section that contains a title, description and optionally a button.
 */
@Composable
internal fun OnboardingTextSection(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    slimWidth: Boolean = true,
    buttonLabel: String? = null,
    onClick: (() -> Unit)? = null
) {
    val titlePadding = if(slimWidth) PaddingValues(horizontal = 48.dp)
        else PaddingValues(horizontal = 12.dp)
    val descriptionPadding = if(slimWidth) PaddingValues(horizontal = 24.dp)
        else PaddingValues(horizontal = 6.dp)

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        Text(
            modifier = Modifier
                .padding(titlePadding),
            text = title,
            style = OnboardingStyles.screenTitle
        )
        Text(
            modifier = Modifier
                .padding(descriptionPadding),
            text = description,
            style = OnboardingStyles.screenDescription
        )

        buttonLabel?.let {
            OutlinedButton(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .width(300.dp),
                onClick = { onClick?.let { it() } }
            ) {
                Text(buttonLabel)
            }
        }

    }
}

/**
 * Defines the layout of an onboarding screen that has an illustration, a title and a description.
 */
@Composable
internal fun SimpleOnboardingLayout(
    modifier: Modifier,
    imagePainter: Painter,
    contentDescription: String,
    title: String,
    description: String,
    continueLabel: String?,
    onContinue: () -> Unit
) {
    BaseOnboardingLayout(
        modifier = modifier,
        primary = {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = imagePainter,
                contentDescription = contentDescription
            )
        },
        primaryPadding = true,
        secondary = {
            OnboardingTextSection(
                modifier = Modifier
                    .weight(1F),
                title = title,
                description = description,
                buttonLabel = continueLabel,
                onClick = onContinue
            )
        }
    )
}