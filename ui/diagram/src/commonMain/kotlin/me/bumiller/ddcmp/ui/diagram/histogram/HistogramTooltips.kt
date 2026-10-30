package me.bumiller.ddcmp.ui.diagram.histogram

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dream_diary_cmp.ui.diagram.generated.resources.Res
import dream_diary_cmp.ui.diagram.generated.resources.tooltip_dream_length_1
import dream_diary_cmp.ui.diagram.generated.resources.tooltip_dream_length_2
import dream_diary_cmp.ui.diagram.generated.resources.tooltip_pcount_1
import dream_diary_cmp.ui.diagram.generated.resources.tooltip_pcount_2
import dream_diary_cmp.ui.diagram.generated.resources.tooltip_time_1
import dream_diary_cmp.ui.diagram.generated.resources.tooltip_time_2
import dream_diary_cmp.ui.diagram.generated.resources.tooltip_weekday
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import me.bumiller.ddcmp.ui.common.format.formatDayOfWeek
import me.bumiller.ddcmp.ui.common.format.formatLocalDate
import org.jetbrains.compose.resources.stringResource

@Composable
private fun BasicTooltip(
    modifier: Modifier,
    label: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .widthIn(max = 160.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 6.dp,
                    vertical = 4.dp
                ),
            content = label
        )
    }
}

@Composable
fun TimeFrequencyTooltip(
    modifier: Modifier = Modifier,
    from: LocalDate,
    length: DatePeriod,
    dreamCount: Int
) {
    val to = from.plus(length)

    val fromString = formatLocalDate(from)
    val toString = formatLocalDate(to)

    BasicTooltip(
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.tooltip_time_1, fromString, toString),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = stringResource(Res.string.tooltip_time_2, dreamCount),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun WeekdayFrequencyTooltip(
    modifier: Modifier = Modifier,
    from: DayOfWeek,
    dreamCount: Int
) {
    val dowName = formatDayOfWeek(from)

    BasicTooltip(
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.tooltip_weekday, dowName, dreamCount),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun DreamLengthTooltip(
    modifier: Modifier = Modifier,
    lowerLimit: Int,
    upperLimit: Int,
    dreamCount: Int
) {
    BasicTooltip(
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.tooltip_dream_length_1, lowerLimit, upperLimit),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = stringResource(Res.string.tooltip_dream_length_2, dreamCount),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun DreamPersonCountTooltip(
    modifier: Modifier = Modifier,
    personCount: Int,
    dreamCount: Int
) {
    BasicTooltip(
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.tooltip_pcount_1, personCount),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = stringResource(Res.string.tooltip_pcount_2, dreamCount),
            style = MaterialTheme.typography.labelSmall
        )
    }
}