package me.ddcmp.feature.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LineAxis
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dream_diary_cmp.feature.home.generated.resources.Res
import dream_diary_cmp.feature.home.generated.resources.overview_current_week_description_above_average
import dream_diary_cmp.feature.home.generated.resources.overview_current_week_description_average
import dream_diary_cmp.feature.home.generated.resources.overview_current_week_description_below_average
import dream_diary_cmp.feature.home.generated.resources.overview_current_week_description_nodata
import dream_diary_cmp.feature.home.generated.resources.overview_current_week_title_above_average
import dream_diary_cmp.feature.home.generated.resources.overview_current_week_title_average
import dream_diary_cmp.feature.home.generated.resources.overview_current_week_title_below_average
import dream_diary_cmp.feature.home.generated.resources.overview_current_week_title_nodata
import me.bumiller.ddcmp.domain.model.stats.RelativeRelation
import me.bumiller.ddcmp.domain.model.stats.Stat
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun OverviewCurrentWeekCard(
    modifier: Modifier,
    currentWeekRelative: Stat<RelativeRelation>,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .clickable(onClick = onClick),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        if(currentWeekRelative is Stat.Present) {
            DataDisplay(
                modifier = Modifier,
                relative = currentWeekRelative.stat
            )
        } else {
            NoDataDisplay(Modifier)
        }
    }
}

@Composable
private fun NoDataDisplay(
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.overview_current_week_title_nodata),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = stringResource(Res.string.overview_current_week_description_nodata),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun DataDisplay(
    modifier: Modifier,
    relative: RelativeRelation
) {
    val title = when (relative) {
        RelativeRelation.BelowAverage -> Res.string.overview_current_week_title_below_average
        RelativeRelation.Average -> Res.string.overview_current_week_title_average
        RelativeRelation.AboveAverage -> Res.string.overview_current_week_title_above_average
    }.let { stringResource(it) }
    val description = when (relative) {
        RelativeRelation.BelowAverage -> Res.string.overview_current_week_description_below_average
        RelativeRelation.Average -> Res.string.overview_current_week_description_average
        RelativeRelation.AboveAverage -> Res.string.overview_current_week_description_above_average
    }.let { stringResource(it) }

    Row(
        modifier = modifier
            .padding(
                start = 8.dp,
                top = 4.dp,
                bottom = 6.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(64.dp),
            imageVector = Icons.Default.LineAxis,
            contentDescription = null
        )

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
                    .copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}