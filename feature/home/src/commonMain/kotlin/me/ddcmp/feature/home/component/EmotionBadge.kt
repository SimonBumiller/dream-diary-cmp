package me.ddcmp.feature.home.component

import androidx.collection.FloatFloatPair
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toPath
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import kotlinx.coroutines.delay
import me.bumiller.ddcmp.domain.model.Emotion
import me.bumiller.ddcmp.domain.model.EmotionType
import me.bumiller.ddcmp.ui.common.style.color
import me.bumiller.ddcmp.ui.common.style.shape
import me.bumiller.ddcmp.ui.common.translation.translateName

private fun RoundedPolygon.scale(scope: CacheDrawScope) = transformed { x, y ->
    FloatFloatPair(
        x * scope.size.minDimension,
        y * scope.size.minDimension
    )
}

private const val ANIMATION_DURATION = 1000
private const val ANIMATION_WAIT_TIME = 3000L

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AbstractEmotionBadge(
    modifier: Modifier,
    emotions: List<Emotion>,
    label: @Composable (Emotion) -> String = { EmotionType.from(it).translateName },
    display: @Composable (modifier: Modifier, emotion: Emotion, badge: @Composable () -> Unit, label: @Composable () -> Unit) -> Unit
) {
    if (emotions.isEmpty()) return

    var currentIndex by remember { mutableStateOf(0) }

    // Change emotion every 1 second
    LaunchedEffect(emotions) {
        while (true) {
            delay(ANIMATION_WAIT_TIME)
            currentIndex = (currentIndex + 1) % emotions.size
        }
    }

    val currentEmotion = emotions[currentIndex]
    val nextEmotion = emotions[(currentIndex + 1) % emotions.size]

    // Animate color progress (0f → 1f every second)
    val color by animateColorAsState(
        targetValue = EmotionType.from(nextEmotion).color,
        animationSpec = tween(ANIMATION_DURATION),
        label = "colorProgress"
    )

    // Reset animation when index changes
    val animatedProgress = remember(currentIndex) { Animatable(0f) }
    LaunchedEffect(currentIndex) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(
            1f,
            animationSpec = tween(ANIMATION_DURATION)
        )
    }

    display(
        modifier,
        nextEmotion,
        {
            Box(
                modifier = Modifier
                    .drawWithCache {
                        val start = EmotionType.from(currentEmotion).shape
                            .scale(this)

                        val end = EmotionType.from(nextEmotion).shape
                            .scale(this)

                        val morph = Morph(start, end)
                        val morphPath = morph
                            .toPath(progress = animatedProgress.value)

                        onDrawBehind {
                            drawPath(morphPath, color = color)
                        }
                    }
                    .size(16.dp)
            )
        },
        {
            Text(
                text = label(nextEmotion),
                style = MaterialTheme.typography.labelSmall
            )
        }
    )


}

/**
 * A component that displays a set of emotions with a colorful and animated badge.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun SimpleEmotionBadge(
    modifier: Modifier = Modifier,
    emotions: List<Emotion>,
    label: @Composable (Emotion) -> String = { EmotionType.from(it).translateName }
) {
    AbstractEmotionBadge(
        modifier = modifier,
        emotions = emotions,
        label = label,
        display = { m, _, badge, label ->
            Row(
                modifier = m,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                badge()
                label()
            }
        }
    )
}

/**
 * A component that displays a set of emotions with a colorful and animated badge.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ChipEmotionBadge(
    modifier: Modifier = Modifier,
    emotions: List<Emotion>,
    onClick: (Emotion) -> Unit,
    label: @Composable (Emotion) -> String = { EmotionType.from(it).translateName },
    selected: Boolean = false
) {
    AbstractEmotionBadge(
        modifier = modifier,
        emotions = emotions,
        label = label,
        display = { m, emotion, badge, label ->
            InputChip(
                modifier = m,
                selected = selected,
                onClick = { onClick(emotion) },
                label = { label() },
                leadingIcon = { badge() }
            )
        }
    )
}