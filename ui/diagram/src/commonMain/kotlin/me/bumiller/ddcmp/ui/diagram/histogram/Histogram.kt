package me.bumiller.ddcmp.ui.diagram.histogram

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import me.bumiller.ddcmp.domain.model.stats.Histogram
import me.bumiller.ddcmp.ui.common.modifier.rotatedLayout
import kotlin.math.floor
import kotlin.math.roundToInt

@Composable
fun <Bucket, Value : Number, BucketSize> Histogram(
    modifier: Modifier = Modifier,
    meta: Histogram<Bucket, BucketSize, Value>,
    bucketLabel: (@Composable (Bucket) -> String)? = null,
    bucketComparator: (Bucket, Bucket) -> Int = { _, _ -> 0 },
    showAverage: Boolean = true,
    tooltip: (@Composable (Bucket, Value, BucketSize) -> Unit)? = null,
    labelStyle: HistogramLabelStyle = HistogramLabelStyle.AllVisible
) {
    var labelsHeight by remember {
        mutableStateOf<Int?>(null)
    }

    val labelIndices = when (labelStyle) {
        HistogramLabelStyle.AllVisible -> 0 ..< meta.dataMap.size
        is HistogramLabelStyle.PartiallyVisible -> evenlySpaced(meta.dataMap.size, labelStyle.labelCount)
    }

    Row(
        modifier = modifier
            .alpha(
                if (labelsHeight == null) 0F
                else 1F
            )
    ) {
        if (labelsHeight != null) {
            val labelsHeightDp = with(LocalDensity.current) { labelsHeight!!.toDp() }
            HistogramScale(
                modifier = Modifier
                    .padding(bottom = labelsHeightDp),
                meta = meta
            )
        }

        Column(
            modifier = Modifier
                .weight(1F)
        ) {
            HistogramBars(
                modifier = Modifier
                    .weight(1F),
                meta = meta,
                bucketComparator = bucketComparator,
                showAverage = showAverage,
                tooltip = tooltip,
                labelIndices = labelIndices
            )

            // Row containing the histogram labels
            HistogramLabels(
                modifier = Modifier
                    .fillMaxWidth(),
                meta = meta,
                bucketLabel = bucketLabel,
                bucketComparator = bucketComparator,
                onHeightDetermined = { labelsHeight = it },
                labelStyle = labelStyle,
                labelIndices = labelIndices
            )
        }

    }

}

@Composable
private fun HistogramScale(
    modifier: Modifier,
    meta: Histogram<*, *, *>,
    labelCount: Int = 3
) {
    val maxValue = remember(meta.dataMap) {
        meta.dataMap.values.maxOf(Number::toFloat)
    }

    val lastLabel = labelCount - 1

    Column(
        modifier = modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val labelList = (0..<labelCount).toList().reversed()

        for (labelIndex in labelList) {
            val fraction = labelIndex.toFloat() / lastLabel
            val labelValue = fraction * maxValue

            Text(
                text = labelValue.toString(),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }

}

@Composable
private fun <Bucket> HistogramLabels(
    modifier: Modifier,
    meta: Histogram<Bucket, *, *>,
    bucketLabel: (@Composable (Bucket) -> String)?,
    bucketComparator: (Bucket, Bucket) -> Int,
    onHeightDetermined: (height: Int) -> Unit,
    labelStyle: HistogramLabelStyle,
    labelIndices: Iterable<Int>
) {
    Row(
        modifier = modifier
            .onGloballyPositioned {
                it.size.height.let(onHeightDetermined)
            },
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = labelStyle.arrangement
    ) {
        meta.dataMap
            .keys
            .sortedWith(bucketComparator)
            .filterIndexed { index, _ -> index in labelIndices }
            .forEach { key ->
                bucketLabel?.invoke(key)?.let { label ->
                    Text(
                        modifier = labelStyle.baseModifier(this)
                            .rotatedLayout(labelStyle.rotation)
                            .rotate(labelStyle.rotation),
                        text = label,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
    }
}

class CenterSpacedArrangement : Arrangement.Horizontal {
    override fun Density.arrange(
        totalSize: Int,
        sizes: IntArray,
        layoutDirection: LayoutDirection,
        outPositions: IntArray,
    ) {
        // Start with normal SpaceBetween positioning
        if (sizes.size == 1) {
            outPositions[0] = -sizes[0] / 2
            return
        }

        val space = (totalSize - sizes.sum()) / (sizes.size - 1)

        var position = 0
        sizes.indices.forEach { i ->
            outPositions[i] = position

            position += sizes[i] + space
        }

        // Center only the first item on the left edge
        outPositions[0] -= sizes[0] / 2
    }
}

    /**
 * Style configuration for the labels of the histogram.
 */
sealed class HistogramLabelStyle(
    internal val rotation: Float,
    internal val arrangement: Arrangement.Horizontal,
    internal val baseModifier: @Composable RowScope.() -> Modifier
) {

    /**
     * All labels are visible.
     */
    data object AllVisible: HistogramLabelStyle(
        rotation = -45.0F,
        arrangement = Arrangement.spacedBy(8.dp),
        baseModifier = { Modifier.weight(1F) }
    )

    /**
     * Only [labelCount] labels are visible.
     */
    data class PartiallyVisible(
        val labelCount: Int
    ): HistogramLabelStyle(
        rotation = -45.0F,
        arrangement = CenterSpacedArrangement(),
        baseModifier = { Modifier }
    )

}

private fun ContentDrawScope.drawAverageLine(average: Float, max: Float, color: Color) {
    val averageRelative = average / max
    val averageHeight = size.height * averageRelative

    val start = Offset(0F, size.height - averageHeight)
    val end = Offset(size.width, size.height - averageHeight)

    val pathEffect = floatArrayOf(12.5F, 12.5F).let(PathEffect::dashPathEffect)

    this.drawLine(
        color = color,
        start = start,
        end = end,
        pathEffect = pathEffect
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <Bucket, BucketSize, Value : Number> HistogramBars(
    modifier: Modifier,
    meta: Histogram<Bucket, BucketSize, Value>,
    bucketComparator: (Bucket, Bucket) -> Int,
    showAverage: Boolean,
    tooltip: (@Composable (Bucket, Value, BucketSize) -> Unit)? = null,
    labelIndices: Iterable<Int>
) {
    val maxValue = remember(meta.dataMap) {
        meta.dataMap.values.maxOf(Number::toFloat)
    }
    val average = remember(meta) { meta.average }

    val averageColor = MaterialTheme.colorScheme.secondary

    val sortedKeys = remember(meta.dataMap) {
        meta.dataMap.keys.sortedWith(bucketComparator)
    }

    var selectedKey by remember {
        mutableStateOf<Bucket?>(null)
    }

    BoxWithConstraints(
        modifier = modifier
            .drawWithContent {
                drawContent()

                if (showAverage) {
                    drawAverageLine(average, maxValue, averageColor)
                }

//                val barCount = meta.histogram.size
//                val spacing = meta.histogram.size.barCountToSpacing().toPx()
//                val barWidth = (size.width - spacing * (barCount - 1)) / barCount
//
//                labelIndices.forEach { index ->
//                    if (index in 0 until barCount) {
//                        val x = index * (barWidth + spacing) + barWidth / 2f
//
//                        drawLine(
//                            color = Color.White,
//                            start = Offset(x, size.height),
//                            end = Offset(x, size.height + 6.dp.toPx()),
//                            strokeWidth = 2.dp.toPx()
//                        )
//                    }
//                }
            }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()

                        var newSelected: Bucket? = null

                        for (change in event.changes) {
                            val x = change.position.x
                            val y = change.position.y

                            if (x !in 0F..size.width.toFloat() || y !in 0F..size.height.toFloat())
                                newSelected = null
                            else {
                                val relativeX = x / size.width
                                val keyIndex = floor(relativeX * meta.dataMap.size).toInt()
                                newSelected = sortedKeys[keyIndex]
                            }

                        }
                        selectedKey = newSelected
                    }
                }
            }
    ) {
        val maxHeight = maxHeight

        // Row containing the histogram bars
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(meta.dataMap.size.barCountToSpacing())
        ) {
            sortedKeys
                .forEachIndexed { index, key ->
                    val value = meta.dataMap[key]!!

                    val relativeValue = value.toFloat() / maxValue
                    val height = maxHeight * relativeValue

                    HistogramBar(
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxHeight(),
                        height = height,
                        selected = key == selectedKey,
                        tooltip = tooltip?.let {
                            { it(key, value, meta.bucketSize) }
                        },
                        unselect = {
                            selectedKey = null
                        },
                        showLabel = index in labelIndices
                    )
                }
        }
    }
}

fun evenlySpaced(size: Int, targetSize: Int): List<Int> {
    require(targetSize in 1..size)

    if (targetSize == 1) return listOf(0)

    return (0 until targetSize).map { i ->
        (i.toDouble() * (size - 1) / (targetSize - 1)).roundToInt()
    }
}

private fun Int.barCountToSpacing(): Dp =
    if (this <= 8) 8.dp
    else if (this <= 16) 6.dp
    else if (this <= 32) 2.dp
    else 1.dp

@Composable
private fun HistogramBar(
    modifier: Modifier = Modifier,
    height: Dp,
    selected: Boolean,
    showLabel: Boolean,
    tooltip: (@Composable () -> Unit)? = null,
    unselect: () -> Unit
) {
    val shape = RoundedCornerShape(
        topStart = 8.dp,
        topEnd = 8.dp
    )

    val background =
        if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.primaryContainer

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .height(height)
                .fillMaxWidth()
                .clip(shape)
                .background(background)
        )

        if (showLabel) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 6.dp)
                    .width(2.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(
                        bottomStart = 6.dp, bottomEnd = 6.dp
                    ))
                    .background(MaterialTheme.colorScheme.onSurface)
            )
        }

        if (selected && tooltip != null) {
            Popup(
                onDismissRequest = unselect,
                alignment = Alignment.Center,
                content = tooltip
            )
        }
    }

}