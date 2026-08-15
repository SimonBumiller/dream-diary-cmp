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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.bumiller.ddcmp.domain.model.stats.HistogramMeta
import me.bumiller.ddcmp.ui.common.modifier.rotatedLayout

@Composable
fun <Bucket, Value : Number> Histogram(
    modifier: Modifier = Modifier,
    meta: HistogramMeta<Bucket, *, Value>,
    bucketLabel: (@Composable (Bucket) -> String)? = null,
    bucketComparator: (Bucket, Bucket) -> Int = { _, _ -> 0 },
    showAverage: Boolean = true
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
                showAverage = showAverage
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

@Composable
private fun <Bucket> HistogramBars(
    modifier: Modifier,
    meta: HistogramMeta<Bucket, *, *>,
    bucketComparator: (Bucket, Bucket) -> Int,
    showAverage: Boolean
) {
    val maxValue = remember(meta.histogram) {
        meta.histogram.values.maxOf(Number::toFloat)
    }
    val average = remember(meta) { meta.average }

    val averageColor = MaterialTheme.colorScheme.secondary

    BoxWithConstraints(
        modifier = modifier
            .drawWithContent {
                drawContent()

                if (showAverage) {
                    drawAverageLine(average, maxValue, averageColor)
                }
            }
    ) {
        val maxHeight = maxHeight

        // Row containing the histogram bars
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            meta.histogram
                .keys
                .sortedWith(bucketComparator)
                .forEach { key ->
                    val value = meta.histogram[key]!!

                    val relativeValue = value.toFloat() / maxValue
                    val height = maxHeight * relativeValue

                    HistogramBar(
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxHeight(),
                        height = height
                    )
                }
        }
    }
}

@Composable
private fun HistogramBar(
    modifier: Modifier = Modifier,
    height: Dp
) {
    val shape = RoundedCornerShape(
        topStart = 8.dp,
        topEnd = 8.dp
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Bottom)
    ) {
        Box(
            modifier = Modifier
                .height(height)
                .fillMaxWidth()
                .clip(shape)
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
    }

}