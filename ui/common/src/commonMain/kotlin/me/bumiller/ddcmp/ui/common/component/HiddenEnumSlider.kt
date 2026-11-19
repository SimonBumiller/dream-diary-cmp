package me.bumiller.ddcmp.ui.common.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import kotlin.enums.EnumEntries

@Composable
public fun <T: Enum<T>> HiddenEnumSlider(
    visible: Boolean,
    current: T,
    toggleVisibility: () -> Unit,
    title: String,
    description: @Composable (T) -> String,
    name: @Composable (T) -> String,
    entries: EnumEntries<T>,
    onChange: (T) -> Unit,
    fromOrdinal: (Int) -> T
) {
    val clearnessRotation by animateFloatAsState(
        targetValue = if (visible) 0F else -90F
    )
    val clearnessAlpha by animateFloatAsState(
        targetValue = if (visible) 1F else 0.39F
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 4.dp,
                bottom = 12.dp
            ),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = toggleVisibility)
        ) {
            Icon(
                modifier = Modifier.rotate(clearnessRotation),
                imageVector = Icons.Default.ArrowDropDown,
                tint = LocalContentColor.current.copy(alpha = clearnessAlpha),
                contentDescription = null
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMediumEmphasized,
                color = LocalContentColor.current.copy(alpha = clearnessAlpha)
            )
        }

        AnimatedVisibility(visible = visible) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = description(current),
                    style = MaterialTheme.typography.bodySmall
                )

                EnumSlider(
                    current = current,
                    entries = entries,
                    fromOrdinal = fromOrdinal,
                    onChange = onChange,
                    name = { name(it) }
                )
            }
        }
    }
}