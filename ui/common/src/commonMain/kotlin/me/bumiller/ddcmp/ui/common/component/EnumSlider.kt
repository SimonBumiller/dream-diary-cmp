package me.bumiller.ddcmp.ui.common.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Label
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.enums.EnumEntries
import kotlin.math.roundToInt

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun <T: Enum<T>> EnumSlider(
    current: T,
    entries: EnumEntries<T>,
    fromOrdinal: (Int) -> T,
    onChange: (T) -> Unit,
    name: @Composable (T) -> String
) {
    val interactionSource = remember { MutableInteractionSource() }
    val sliderState = rememberSliderState(
        value = current.ordinal.toFloat(),
        steps = entries.size - 2,
        trackRange = 0F..(entries.size - 1).toFloat()
    )

    Slider(
        state = sliderState,
        onValueChange = {
            sliderState.value = it
            val enumEntry = fromOrdinal(it.roundToInt())
            onChange(enumEntry)
        },
        interactionSource = interactionSource,
        thumb = {
            Label(
                label = {
                    PlainTooltip(
                        modifier = Modifier
                            .sizeIn(minWidth = 45.dp, minHeight = 25.dp)
                            .wrapContentWidth()
                    ) {
                        Text(name(fromOrdinal(it.value.roundToInt())))
                    }
                },
                interactionSource = interactionSource,
                // Set isPersistent = true to keep the floating label always visible
                isPersistent = false
            ) {
                SliderDefaults.Thumb(
                    interactionSource = interactionSource
                )
            }
        }
    )
}