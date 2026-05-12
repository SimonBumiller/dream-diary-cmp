package me.bumiller.ddcmp.ui.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.bumiller.ddcmp.domain.model.Confidentiality
import me.bumiller.ddcmp.ui.common.style.icon
import me.bumiller.ddcmp.ui.common.translation.translateName

@Composable
fun ConfidentialitySelector(
    modifier: Modifier = Modifier,
    confidentiality: Confidentiality,
    onSelect: (Confidentiality) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Confidentiality.entries.forEach { confid ->
                val selected = confid == confidentiality
                Column(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .clickable(
                            onClick = { onSelect(confid) }
                        )
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = confid.icon,
                        contentDescription = confid.translateName,
                        tint = if (selected)
                            MaterialTheme.colorScheme.primary
                        else LocalContentColor.current
                    )

                    Text(
                        text = confid.translateName,
                        style = MaterialTheme.typography.labelLarge.run {
                            if (selected) {
                                copy(
                                    fontWeight = FontWeight.ExtraBold
                                )
                            } else this
                        },
                        color = if (selected)
                            MaterialTheme.colorScheme.primary
                        else LocalContentColor.current
                    )
                }
            }
        }

    }
}