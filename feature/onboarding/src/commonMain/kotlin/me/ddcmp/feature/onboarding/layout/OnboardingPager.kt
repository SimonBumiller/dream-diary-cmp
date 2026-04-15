package me.ddcmp.feature.onboarding.layout

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.bumiller.ddcmp.ui.common.LocalWindowSizeClass
import me.ddcmp.feature.onboarding.OnboardingStyles

@Composable
internal fun <Screen> OnboardingPager(
    modifier: Modifier,
    screens: List<Screen>,
    content: @Composable (Screen) -> Unit
) {
    val state = rememberPagerState { screens.size }
    val sizeClass = LocalWindowSizeClass.current

    if (sizeClass.doVertical) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                modifier = Modifier
                    .weight(1F),
                state = state,
            ) { page ->
                content(screens[page])
            }

            PageIndicator(
                modifier = Modifier
                    .padding(bottom = 12.dp),
                numberOfPages = screens.size,
                selectedPage = state.currentPage,
                vertical = false
            )
        }
    } else {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            VerticalPager(
                modifier = Modifier
                    .weight(1F),
                state = state,
            ) { page ->
                content(screens[page])
            }

            PageIndicator(
                modifier = Modifier
                    .padding(end = 12.dp),
                numberOfPages = screens.size,
                selectedPage = state.currentPage,
                vertical = true
            )
        }
    }
}

/*
 * Adapted from https://medium.com/make-apps-simple/page-indicator-with-jetpack-compose-using-canvas-and-animations-a851ea4cf749
 */

@Composable
fun PageIndicatorView(
    isSelected: Boolean,
    animationDurationInMillis: Int,
    modifier: Modifier = Modifier,
    vertical: Boolean
) {
    val color: Color by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
        else OnboardingStyles.pagerIndicatorInactive,
        animationSpec = tween(
            durationMillis = animationDurationInMillis,
        )
    )
    val axis: Dp by animateDpAsState(
        targetValue = if (isSelected) 24.dp else 12.dp,
        animationSpec = tween(
            durationMillis = animationDurationInMillis,
        )
    )

    Canvas(
        modifier = modifier
            .size(
                width = if(!vertical) axis else 12.dp,
                height = if(vertical) axis else 12.dp,
            ),
    ) {
        drawRoundRect(
            color = color,
            topLeft = Offset.Zero,
            size = Size(
                width = if(!vertical) axis.toPx() else 12.dp.toPx(),
                height = if(vertical) axis.toPx() else 12.dp.toPx(),
            ),
            cornerRadius = CornerRadius(
                x = 12.dp.toPx(),
                y = 12.dp.toPx(),
            ),
        )
    }
}

@Composable
fun PageIndicator(
    numberOfPages: Int,
    modifier: Modifier = Modifier,
    selectedPage: Int = 0,
    animationDurationInMillis: Int = 300,
    vertical: Boolean
) {
    if (vertical) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier,
        ) {
            for (i in 0 until numberOfPages) {
                val isSelected = i == selectedPage
                PageIndicatorView(
                    isSelected = isSelected,
                    animationDurationInMillis = animationDurationInMillis,
                    vertical = vertical
                )
            }
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier,
        ) {
            for (i in 0 until numberOfPages) {
                val isSelected = i == selectedPage
                PageIndicatorView(
                    isSelected = isSelected,
                    animationDurationInMillis = animationDurationInMillis,
                    vertical = vertical
                )
            }
        }
    }


}