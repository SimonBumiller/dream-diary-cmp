package me.bumiller.ddcmp.ui.common.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import me.bumiller.ddcmp.domain.model.dream.Person
import me.bumiller.ddcmp.domain.model.dream.Tag

@Composable
public fun AppTagInputChip(
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
private fun colorsForTag(tag: Tag) = InputChipDefaults.inputChipColors(
    labelColor = Color(tag.color),
    leadingIconColor = Color(tag.color),
    trailingIconColor = Color(tag.color),
)

@Composable
private fun borderForTag(tag: Tag, enabled: Boolean, selected: Boolean) = InputChipDefaults.inputChipBorder(
    enabled = enabled,
    selected = selected,
    borderColor = Color(tag.color)
)

@Composable
public fun PersonInputChip(
    modifier: Modifier = Modifier,
    person: Person,
    tags: List<Tag>,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    selected: Boolean = false,
    enabled: Boolean = true,
) {
    val tag = tags.firstOrNull()

    AppInputChip(
        modifier = modifier
            .padding(horizontal = 6.dp),
        label = {
            Text(person.name)
        },
        onClick = onClick,
        onLongClick = onLongClick,
        selected = selected,
        border = tag?.let { borderForTag(it, enabled, selected) } ?: InputChipDefaults.inputChipBorder(enabled, selected),
        colors = tag?.let { colorsForTag(it) } ?: InputChipDefaults.inputChipColors(),
    )
}

@Composable
public fun AppInputChip(
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
    contentPadding: PaddingValues =
        InputChipDefaults.contentPadding(avatar != null, leadingIcon != null, trailingIcon != null),
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
            horizontalArrangement = InputChipDefaults.horizontalArrangement(),
            contentPadding,
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