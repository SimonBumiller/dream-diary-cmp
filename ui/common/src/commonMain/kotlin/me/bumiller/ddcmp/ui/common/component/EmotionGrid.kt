package me.bumiller.ddcmp.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import me.bumiller.ddcmp.domain.model.EmotionCoordinate
import me.bumiller.ddcmp.domain.model.EmotionType
import me.bumiller.ddcmp.domain.model.EmotionType.Anger
import me.bumiller.ddcmp.domain.model.EmotionType.Joy
import me.bumiller.ddcmp.domain.model.EmotionType.Pleasure
import me.bumiller.ddcmp.domain.model.EmotionType.Sadness
import me.bumiller.ddcmp.ui.common.style.color
import me.bumiller.ddcmp.ui.common.translation.translateName
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun <T : EmotionCoordinate> EmotionGrid(
    modifier: Modifier = Modifier,
    existingPoints: Set<T> = emptySet(),
    onClickClosest: (T?) -> Unit = {}
) {
    val dotColor = MaterialTheme.colorScheme.outline
    val minimumInteractiveSize =
        with(LocalDensity.current) { LocalMinimumInteractiveComponentSize.current.toPx() }

    Column(
        modifier = modifier
            .aspectRatio(1F)
            .border(
                width = 3.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            ).minimumInteractiveComponentSize()
            .drawWithContent(
                onDraw = {
                    drawContent()
                    existingPoints.forEach { emotionCoordinate ->
                        val relX = emotionCoordinate.valence
                        val relY = emotionCoordinate.arousal

                        val x = (relX.value + 1) / 2 * size.width
                        val y = (1 - (relY.value + 1) / 2) * size.height

                        drawCircle(
                            color = dotColor,
                            radius = 8F,
                            center = Offset(x, y)
                        )
                    }
                }
            )
            .pointerInput(Unit) {
                detectTapGestures { pos ->
                    val valence = (2 * pos.x / size.width) - 1
                    val arousal = 1 - (2 * pos.y / size.height)

                    val (closest, relDistance) = existingPoints
                        .map {
                            val xDif = abs(it.valence.value - valence)
                            val yDif = abs(it.arousal.value - arousal)
                            val dif = sqrt(xDif.pow(2) + yDif.pow(2))
                            it to dif
                        }
                        .minBy { (_, dif) -> dif }

                    val actualDistance = minOf(size.width, size.height) / 2 * relDistance

                    println("Captured click on (${pos.x}, ${pos.y}), transformed to ($valence, $arousal) and got distance $actualDistance to emotion $closest")

                    if(actualDistance <= minimumInteractiveSize)
                        onClickClosest(closest)
                    else onClickClosest(null)
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        ) {
            TypeBox(Anger)
            TypeBox(Joy)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        ) {
            TypeBox(Sadness)
            TypeBox(Pleasure)
        }
    }
}

private fun Color.dimmed() = copy(alpha = 0.8F).compositeOver(Color.White)
private fun Color.dimmedStrong() = copy(alpha = 0.4F).compositeOver(Color.White)

@Composable
private fun RowScope.TypeBox(
    type: EmotionType,
    modifier: Modifier = Modifier
) {
    val dimmedColor = type.color.dimmed()
    val dimmedColorStrong = type.color.dimmedStrong()

    val padding = when (type) {
        Joy -> PaddingValues(2.dp, 0.dp, 8.dp, 0.dp)
        Pleasure -> PaddingValues(2.dp, 0.dp, 0.dp, 0.dp)
        Sadness -> PaddingValues(8.dp, 0.dp, 2.dp, 0.dp)
        Anger -> PaddingValues(0.dp, 0.dp, 2.dp, 0.dp)
    }

    Box(
        modifier = modifier
            .weight(1F)
            .fillMaxHeight()
            .background(dimmedColor)
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = type.translateName,
            style = MaterialTheme.typography.displaySmall,
            color = dimmedColorStrong
        )

        when (type) {
            Joy -> {
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                    text = "High Valence",
                    color = dimmedColorStrong
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 4.dp),
                    text = "Arousal",
                    color = dimmedColorStrong
                )
            }

            Pleasure -> {
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 4.dp),
                    text = "Arousal",
                    color = dimmedColorStrong
                )
            }

            Sadness -> {
                Text(
                    modifier = Modifier
                        .align(Alignment.TopStart),
                    text = "Low Valence",
                    color = dimmedColorStrong
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 4.dp),
                    text = "Low",
                    color = dimmedColorStrong
                )
            }

            Anger -> {

                Text(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 4.dp),
                    text = "High",
                    color = dimmedColorStrong
                )
            }
        }
    }
}