package me.bumiller.ddcmp.ui.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.bumiller.ddcmp.domain.model.Confidentiality
import me.bumiller.ddcmp.ui.common.style.DisabledContent
import me.bumiller.ddcmp.ui.common.translation.translateDescription
import me.bumiller.ddcmp.ui.common.translation.translateName

@Composable
fun <T> RadioSelectors(
    modifier: Modifier = Modifier,
    items: List<T>,
    selected: T? = null,
    onSelect: (T) -> Unit,
    title: @Composable (T) -> String,
    description: @Composable (T) -> String
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach {
            RadioItem(
                modifier = Modifier
                    .fillMaxWidth(),
                item = it,
                selected = it == selected,
                onSelect = { onSelect(it) },
                title = title(it),
                description = description(it)
            )
        }
    }
}

@Composable
private fun <T> RadioItem(
    modifier: Modifier = Modifier,
    item: T,
    selected: Boolean,
    onSelect: () -> Unit,
    title: String,
    description: String
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onSelect),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        DisabledContent(!selected) {
            Column {
                Text(
                    text = title,
                    style = titleStyle
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private val titleStyle: TextStyle
    @Composable get() = MaterialTheme.typography.titleMedium
        .copy(fontWeight = FontWeight.Bold)