package me.ddcmp.feature.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dream_diary_cmp.feature.stats.generated.resources.Res
import dream_diary_cmp.feature.stats.generated.resources.weekly_average_30_days
import dream_diary_cmp.feature.stats.generated.resources.weekly_average_6_months
import dream_diary_cmp.feature.stats.generated.resources.weekly_average_7_days
import dream_diary_cmp.feature.stats.generated.resources.weekly_average_overall
import me.bumiller.ddcmp.domain.model.stats.Stat
import me.bumiller.ddcmp.domain.stats.dream.global.DreamWeeklyFrequencyStatistic.WeeklyFrequency
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round

@Composable
internal fun WeeklyAverageDashboard(
    modifier: Modifier = Modifier,
    weeklyStats: WeeklyFrequency
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AverageDisplay(
            value = weeklyStats.overallAverage,
            label = stringResource(Res.string.weekly_average_overall),
            color = MaterialTheme.colorScheme.primary
        )
        AverageDisplay(
            value = weeklyStats.lastSevenDays,
            label = stringResource(Res.string.weekly_average_7_days),
            color = MaterialTheme.colorScheme.secondary
        )

        if(weeklyStats.lastThirtyDays is Stat.Present) {
            val value = (weeklyStats.lastThirtyDays as Stat.Present<Float>).stat
            AverageDisplay(
                value = value,
                label = stringResource(Res.string.weekly_average_30_days),
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        if(weeklyStats.lastSixMonths is Stat.Present) {
            val value = (weeklyStats.lastSixMonths as Stat.Present<Float>).stat
            AverageDisplay(
                modifier = Modifier
                    .widthIn(max = 64.dp),
                value = value,
                label = stringResource(Res.string.weekly_average_6_months),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

private fun Float.roundToOneDecimal(): Float = round(this * 10) / 10

@Composable
private fun AverageDisplay(
    modifier: Modifier = Modifier,
    value: Float,
    label: String,
    color: Color
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value.roundToOneDecimal().toString(),
            style = MaterialTheme.typography.headlineMedium
                .copy(fontWeight = FontWeight.Bold),
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
    }
}