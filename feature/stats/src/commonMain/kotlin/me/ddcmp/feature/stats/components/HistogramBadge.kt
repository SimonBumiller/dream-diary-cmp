package me.ddcmp.feature.stats.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingDown
import androidx.compose.material.icons.automirrored.outlined.TrendingFlat
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.bumiller.ddcmp.domain.model.stats.HistogramRank

@Composable
internal fun HistogramBadge(
    modifier: Modifier = Modifier,
    rank: HistogramRank,
    label: @Composable (HistogramRank) -> String
) {
    val vector = when (rank) {
        HistogramRank.Lowest, HistogramRank.Low -> Icons.AutoMirrored.Outlined.TrendingDown
        HistogramRank.Average -> Icons.AutoMirrored.Outlined.TrendingFlat
        HistogramRank.High, HistogramRank.Highest -> Icons.AutoMirrored.Outlined.TrendingUp
    }

    SuggestionChip(
        modifier = modifier,
        onClick = {},
        icon = {
            Icon(
                imageVector = vector,
                contentDescription = null
            )
        },
        label = {
            Text(label(rank))
        }
    )
}