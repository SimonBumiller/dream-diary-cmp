package me.bumiller.ddcmp.ui.common.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints
import kotlin.math.PI

fun Modifier.rotatedLayout(
    degrees: Float
) = this.then(
    object : LayoutModifier {
        override fun MeasureScope.measure(
            measurable: Measurable,
            constraints: Constraints
        ): MeasureResult {
            val placeable = measurable.measure(constraints)

            val radians = degrees.toDouble() * PI / 180.0
            val sin = kotlin.math.abs(kotlin.math.sin(radians))
            val cos = kotlin.math.abs(kotlin.math.cos(radians))

            val rotatedWidth =
                (placeable.width * cos + placeable.height * sin).toInt()

            val rotatedHeight =
                (placeable.width * sin + placeable.height * cos).toInt()

            return layout(rotatedWidth, rotatedHeight) {
                placeable.placeRelative(
                    x = (rotatedWidth - placeable.width) / 2,
                    y = (rotatedHeight - placeable.height) / 2
                )
            }
        }
    }
)