package me.bumiller.ddcmp.ui.diagram.histogram

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import me.bumiller.ddcmp.domain.model.stats.HistogramMeta
import me.bumiller.ddcmp.ui.common.modifier.rotatedLayout
import kotlin.math.floor

@Composable
fun <Bucket, Value : Number, BucketSize> Histogram(
    modifier: Modifier = Modifier,
    meta: HistogramMeta<Bucket, BucketSize, Value>,
    bucketLabel: (@Composable (Bucket) -> String)? = null,
    bucketComparator: (Bucket, Bucket) -> Int = { _, _ -> 0 },
    showAverage: Boolean = true,
    tooltip: (@Composable (Bucket, Value, BucketSize) -> Unit)? = null,
) {
    var labelsHeight by remember {
        mutableStateOf<Int?>(null)
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
                tooltip = tooltip
            )

            // Row containing the histogram labels
            HistogramLabels(
                modifier = Modifier
                    .fillMaxWidth(),
                meta = meta,
                bucketLabel = bucketLabel,
                bucketComparator = bucketComparator,
                onHeightDetermined = { labelsHeight = it }
            )
        }

    }

}

@Composable
private fun HistogramScale(
    modifier: Modifier,
    meta: HistogramMeta<*, *, *>,
    labelCount: Int = 3
) {
    val maxValue = remember(meta.histogram) {
        meta.histogram.values.maxOf(Number::toFloat)
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
    meta: HistogramMeta<Bucket, *, *>,
    bucketLabel: (@Composable (Bucket) -> String)?,
    bucketComparator: (Bucket, Bucket) -> Int,
    onHeightDetermined: (height: Int) -> Unit
) {
    Row(
        modifier = modifier
            .onGloballyPositioned {
                it.size.height.let(onHeightDetermined)
            },
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        meta.histogram
            .keys
            .sortedWith(bucketComparator)
            .forEach { key ->
                bucketLabel?.invoke(key)?.let { label ->
                    Text(
                        modifier = Modifier
                            .weight(1F)
                            .rotatedLayout(-45F)
                            .rotate(-45F),
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
    meta: HistogramMeta<Bucket, BucketSize, Value>,
    bucketComparator: (Bucket, Bucket) -> Int,
    showAverage: Boolean,
    tooltip: (@Composable (Bucket, Value, BucketSize) -> Unit)? = null,
) {
    val maxValue = remember(meta.histogram) {
        meta.histogram.values.maxOf(Number::toFloat)
    }
    val average = remember(meta) { meta.average }

    val averageColor = MaterialTheme.colorScheme.secondary

    val sortedKeys = remember(meta.histogram) {
        meta.histogram.keys.sortedWith(bucketComparator)
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
                                val keyIndex = floor(relativeX * meta.histogram.size).toInt()
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
            horizontalArrangement = Arrangement.spacedBy(meta.histogram.size.barCountToSpacing())
        ) {
            sortedKeys
                .forEach { key ->
                    val value = meta.histogram[key]!!

                    val relativeValue = value.toFloat() / maxValue
                    val height = maxHeight * relativeValue

                    println("Got key $key and value $value and will have a relative heigh of $relativeValue with absolute mapping $height")

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
                        }
                    )
                }
        }
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

        if (selected && tooltip != null) {
            Popup(
                onDismissRequest = unselect,
                alignment = Alignment.Center,
                content = tooltip
            )
        }
    }

}