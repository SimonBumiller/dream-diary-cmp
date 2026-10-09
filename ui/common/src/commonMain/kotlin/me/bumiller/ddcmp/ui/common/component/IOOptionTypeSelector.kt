package me.bumiller.ddcmp.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.bumiller.ddcmp.ui.common.model.IOOptionType
import me.bumiller.ddcmp.ui.common.translation.translateDescription
import me.bumiller.ddcmp.ui.common.translation.translateName

@Composable
fun IOOptionTypeSelection(
    modifier: Modifier,
    type: IOOptionType,
    onTypeChange: (IOOptionType) -> Unit
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {
        IOOptionButton(
            modifier = Modifier
                .weight(1F)
                .fillMaxHeight(),
            type = IOOptionType.Normal,
            shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
            onSelect = { onTypeChange(IOOptionType.Normal) },
            selected = type == IOOptionType.Normal,
            recommended = true
        )
        IOOptionButton(
            modifier = Modifier
                .weight(1F)
                .fillMaxHeight(),
            type = IOOptionType.Legacy,
            shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
            onSelect = { onTypeChange(IOOptionType.Legacy) },
            selected = type == IOOptionType.Legacy
        )
    }
}

@Composable
private fun IOOptionButton(
    modifier: Modifier,
    type: IOOptionType,
    shape: CornerBasedShape,
    onSelect: () -> Unit,
    selected: Boolean,
    recommended: Boolean = false
) {
    val bgColor = when (selected) {
        false -> Color.Transparent
        true -> MaterialTheme.colorScheme.primaryContainer
    }

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = shape
            )
            .clip(shape)
            .clickable(onClick = onSelect)
            .background(bgColor)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = type.translateName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            if (recommended) {
                Icon(
                    modifier = Modifier
                        .size(16.dp),
                    imageVector = Icons.Outlined.StarBorder,
                    contentDescription = null
                )
            }
        }

        Text(
            text = type.translateDescription,
            style = MaterialTheme.typography.bodySmall
        )
    }
}