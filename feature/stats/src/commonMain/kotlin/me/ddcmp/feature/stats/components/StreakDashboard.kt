package me.ddcmp.feature.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dream_diary_cmp.feature.stats.generated.resources.Res
import dream_diary_cmp.feature.stats.generated.resources.streak_dashboard_interval
import dream_diary_cmp.feature.stats.generated.resources.streak_dashboard_interval_none
import dream_diary_cmp.feature.stats.generated.resources.streak_dashboard_interval_single
import dream_diary_cmp.feature.stats.generated.resources.streak_dashboard_value
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.stats.dream.global.DreamStreakStatistic.StreakStatistic
import me.bumiller.ddcmp.ui.common.format.formatLocalDate
import me.bumiller.ddcmp.ui.common.format.formatLocalDateYearIndependent
import me.bumiller.ddcmp.ui.common.style.DisabledContent
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
internal fun StreakDashboard(
    modifier: Modifier = Modifier,
    streak: StreakStatistic
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StreakDisplay(
            streak = streak.current,
            color = MaterialTheme.colorScheme.primary,
            label = "Current Streak"
        )

        HorizontalDivider()

        StreakDisplay(
            streak = streak.longest,
            color = MaterialTheme.colorScheme.secondary,
            label = "Longest Streak"
        )
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun StreakDisplay(
    modifier: Modifier = Modifier,
    streak: List<LocalDate>,
    color: Color,
    label: String
) {
    val length = streak.size

    val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year

    val intervalString = when (streak.size) {
        0 -> stringResource(Res.string.streak_dashboard_interval_none)
        1 -> {
            val day = streak.single()
            val showYear = currentYear == day.year
            val dayString = if (showYear) formatLocalDate(day)
            else formatLocalDateYearIndependent(day)

            stringResource(Res.string.streak_dashboard_interval_single, dayString)
        }

        else -> {
            val from = streak.first()
            val to = streak.last()

            val fromShowYear = currentYear != from.year
            val toShowYear = currentYear != to.year

            val fromString = if (fromShowYear) formatLocalDate(from)
            else formatLocalDateYearIndependent(from)
            val toString = if (toShowYear) formatLocalDate(to)
            else formatLocalDateYearIndependent(to)

            stringResource(Res.string.streak_dashboard_interval, fromString, toString)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            modifier = Modifier
                .weight(3F),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge
            )
            DisabledContent {
                Text(
                    text = intervalString,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
            }
        }

        Text(
            modifier = Modifier
                .weight(2F),
            text = stringResource(Res.string.streak_dashboard_value, length),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color,
            textAlign = TextAlign.Center
        )
    }
}