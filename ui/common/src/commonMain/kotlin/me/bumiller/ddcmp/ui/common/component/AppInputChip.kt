package me.bumiller.ddcmp.ui.common.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SelectableChipElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import me.bumiller.ddcmp.domain.model.dream.Tag

@Composable
fun AppTagInputChip(
    selected: Boolean,
    tag: Tag,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    avatar: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = InputChipDefaults.shape,
    elevation: SelectableChipElevation? = InputChipDefaults.inputChipElevation(),
    interactionSource: MutableInteractionSource? = null
) {
    AppInputChip(
        selected = selected,
        onClick = onClick,
        onLongClick = onLongClick,
        label = { Text(tag.name) },
        modifier = modifier,
        enabled = enabled,
        leadingIcon = leadingIcon,
        avatar = avatar,
        trailingIcon = trailingIcon,
        shape = shape,
        colors = InputChipDefaults.inputChipColors(
            labelColor = Color(tag.color),
            leadingIconColor = Color(tag.color),
            trailingIconColor = Color(tag.color),
        ),
        elevation = elevation,
        border = InputChipDefaults.inputChipBorder(
            enabled = enabled,
            selected = selected,
            borderColor = Color(tag.color)
        ),
        interactionSource = interactionSource
    )
}

@Composable
fun AppInputChip(
    selected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    avatar: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = InputChipDefaults.shape,
    colors: SelectableChipColors = InputChipDefaults.inputChipColors(),
    elevation: SelectableChipElevation? = InputChipDefaults.inputChipElevation(),
    border: BorderStroke? = InputChipDefaults.inputChipBorder(enabled, selected),
    interactionSource: MutableInteractionSource? = null
) {
    val intSource = interactionSource ?: remember { MutableInteractionSource() }

    Box {
        InputChip(
            selected,
            {},
            label,
            modifier,
            enabled,
            leadingIcon,
            avatar,
            trailingIcon,
            shape,
            colors,
            elevation,
            border,
            intSource
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .combinedClickable(
                    interactionSource = intSource,
                    indication = null,
                    onLongClick = onLongClick,
                    onClick = onClick
                )
        )
    }
}