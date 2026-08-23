package me.ddcmp.feature.stats.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.stats.generated.resources.Res
import dream_diary_cmp.feature.stats.generated.resources.global_dream_streak_description
import dream_diary_cmp.feature.stats.generated.resources.global_dream_streak_title
import dream_diary_cmp.feature.stats.generated.resources.global_dream_time_frequency_description
import dream_diary_cmp.feature.stats.generated.resources.global_dream_time_frequency_title
import dream_diary_cmp.feature.stats.generated.resources.global_dream_time_frequency_tooltip_1
import dream_diary_cmp.feature.stats.generated.resources.global_dream_time_frequency_tooltip_2
import dream_diary_cmp.feature.stats.generated.resources.global_dream_title
import dream_diary_cmp.feature.stats.generated.resources.global_dream_weekday_frequency_description
import dream_diary_cmp.feature.stats.generated.resources.global_dream_weekday_frequency_title
import dream_diary_cmp.feature.stats.generated.resources.global_dream_weekday_frequency_tooltip
import dream_diary_cmp.feature.stats.generated.resources.global_dream_weekly_average_description_30_days
import dream_diary_cmp.feature.stats.generated.resources.global_dream_weekly_average_description_6_months
import dream_diary_cmp.feature.stats.generated.resources.global_dream_weekly_average_description_7_days
import dream_diary_cmp.feature.stats.generated.resources.global_dream_weekly_average_title
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.DayOfWeek.FRIDAY
import kotlinx.datetime.DayOfWeek.MONDAY
import kotlinx.datetime.DayOfWeek.SATURDAY
import kotlinx.datetime.DayOfWeek.SUNDAY
import kotlinx.datetime.DayOfWeek.THURSDAY
import kotlinx.datetime.DayOfWeek.TUESDAY
import kotlinx.datetime.DayOfWeek.WEDNESDAY
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import me.bumiller.ddcmp.domain.model.stats.HistogramMeta
import me.bumiller.ddcmp.domain.model.stats.Stat
import me.bumiller.ddcmp.domain.stats.dream.global.DreamStreakStatistic.StreakStatistic
import me.bumiller.ddcmp.domain.stats.dream.global.DreamWeeklyFrequencyStatistic.WeeklyFrequency
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.dialog.DateRangeDialog
import me.bumiller.ddcmp.ui.common.format.formatDayOfWeek
import me.bumiller.ddcmp.ui.common.format.formatDayOfWeekShort
import me.bumiller.ddcmp.ui.common.format.formatLocalDate
import me.bumiller.ddcmp.ui.common.layout.TopBarWithProgressIndicator
import me.bumiller.ddcmp.ui.common.plus
import me.bumiller.ddcmp.ui.diagram.histogram.Histogram
import me.bumiller.ddcmp.ui.diagram.histogram.HistogramLabelStyle
import me.ddcmp.feature.stats.components.StatCard
import me.ddcmp.feature.stats.components.StreakDashboard
import me.ddcmp.feature.stats.components.WeeklyAverageDashboard
import me.ddcmp.feature.stats.entries.DreamGlobalViewModel.UiEvent
import me.ddcmp.feature.stats.entries.DreamGlobalViewModel.ViewModelEvent
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DreamGlobal(
    onReturn: () -> Unit
) {
    ViewModelScope<UiEvent, ViewModelEvent, DreamGlobalViewModel>(
        onViewModelEvent = {
            when (it) {
                ViewModelEvent.Return -> onReturn()
            }
        }
    ) { vm ->
        val timeFrequency by vm.timeHistogram.collectAsStateWithLifecycle()
        val weekdayFrequency by vm.weekdayHistogram.collectAsStateWithLifecycle()
        val weeklyFrequency by vm.weeklyFrequency.collectAsStateWithLifecycle()
        val streakStatistic by vm.streakStatistic.collectAsStateWithLifecycle()
        val uiState by vm.uiState.collectAsStateWithLifecycle()

        DreamGlobal(timeFrequency, weekdayFrequency, weeklyFrequency, streakStatistic, uiState, vm::onEvent)


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DreamGlobal(
    timeFrequency: Stat<HistogramMeta<LocalDate, DatePeriod, Int>>?,
    weekdayFrequency: Stat<HistogramMeta<DayOfWeek, Unit, Int>>?,
    weeklyFrequency: Stat<WeeklyFrequency>?,
    streakStatistic: Stat<StreakStatistic>?,
    uiState: DreamGlobalViewModel.UiState,
    onEvent: (UiEvent) -> Unit
) {
    val isLoading = listOf(
        timeFrequency,
        weeklyFrequency,
        weekdayFrequency,
        streakStatistic
    ).any { it == null }

    if(uiState.showDateRangeDialog) {
        DateRangeDialog(
            modifier = Modifier,
            onDismiss = { onEvent(UiEvent.ToggleDateRangeDialogVisibility) },
            onRangeSelected = {
                it?.let {
                    onEvent(UiEvent.ChangeDateRange(it))
                }
            },
            currentRange = uiState.dateRange
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBarWithProgressIndicator(
                loading = isLoading
            ) {
                CenterAlignedTopAppBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = {
                        Text(
                            text = stringResource(Res.string.global_dream_title)
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                onEvent(UiEvent.ToggleDateRangeDialogVisibility)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.EditCalendar,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        }
    ) { pv ->
        if (isLoading) return@Scaffold

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv + PaddingValues(horizontal = 8.dp))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier
                    .height(350.dp),
                stat = weekdayFrequency!!,
                title = stringResource(Res.string.global_dream_weekday_frequency_title),
                description = stringResource(Res.string.global_dream_weekday_frequency_description)
            ) { histogram ->
                Histogram(
                    meta = histogram,
                    bucketLabel = { formatDayOfWeekShort(it) },
                    bucketComparator = weekdayOrderComparator,
                    tooltip = { from, count, _ ->
                        WeekdayFrequencyTooltip(
                            from = from,
                            dreamCount = count
                        )
                    }
                )
            }

            val weeklyFrequencyDescription = when (weeklyFrequency) {
                is Stat.Present -> when (weeklyFrequency.stat.lastThirtyDays) {
                    is Stat.Present -> when (weeklyFrequency.stat.lastSixMonths) {
                        is Stat.Present -> stringResource(Res.string.global_dream_weekly_average_description_6_months)
                        is Stat.NotEnoughData -> stringResource(Res.string.global_dream_weekly_average_description_30_days)
                    }

                    is Stat.NotEnoughData -> stringResource(Res.string.global_dream_weekly_average_description_7_days)
                }

                is Stat.NotEnoughData, null -> stringResource(Res.string.global_dream_weekly_average_description_7_days)
            }
            StatCard(
                stat = weeklyFrequency!!,
                title = stringResource(Res.string.global_dream_weekly_average_title),
                description = weeklyFrequencyDescription
            ) { frequency ->

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    WeeklyAverageDashboard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        weeklyStats = frequency
                    )
                }
            }

            StatCard(
                stat = streakStatistic!!,
                title = stringResource(Res.string.global_dream_streak_title),
                description = stringResource(Res.string.global_dream_streak_description)
            ) { streak ->
                StreakDashboard(
                    streak = streak
                )
            }

            StatCard(
                modifier = Modifier
                    .height(350.dp),
                stat = timeFrequency!!,
                title = stringResource(Res.string.global_dream_time_frequency_title),
                description = stringResource(Res.string.global_dream_time_frequency_description)
            ) { histogram ->
                Histogram(
                    meta = histogram,
                    bucketLabel = { it.toString() },
                    bucketComparator = { date1, date2 ->
                        date1.toEpochDays().compareTo(date2.toEpochDays())
                    },
                    tooltip = { from, count, length ->
                        TimeFrequencyTooltip(
                            from = from,
                            length = length,
                            dreamCount = count
                        )
                    },
                    labelStyle = HistogramLabelStyle.PartiallyVisible(3)
                )
            }
        }
    }
}

@Composable
private fun TimeFrequencyTooltip(
    modifier: Modifier = Modifier,
    from: LocalDate,
    length: DatePeriod,
    dreamCount: Int
) {
    val to = from.plus(length)

    val fromString = formatLocalDate(from)
    val toString = formatLocalDate(to)

    ElevatedCard(
        modifier = modifier
            .widthIn(max = 160.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 6.dp,
                    vertical = 4.dp
                )
        ) {
            Text(
                text = stringResource(Res.string.global_dream_time_frequency_tooltip_1, fromString, toString),
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = stringResource(Res.string.global_dream_time_frequency_tooltip_2, dreamCount),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun WeekdayFrequencyTooltip(
    modifier: Modifier = Modifier,
    from: DayOfWeek,
    dreamCount: Int
) {
    val dowName = formatDayOfWeek(from)

    ElevatedCard(
        modifier = modifier
            .widthIn(max = 160.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 6.dp,
                    vertical = 4.dp
                )
        ) {
            Text(
                text = stringResource(Res.string.global_dream_weekday_frequency_tooltip, dowName, dreamCount),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

private val dayOfWeekOrder = listOf(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY)

private val weekdayOrderComparator = { dow1: DayOfWeek, dow2: DayOfWeek ->
    dayOfWeekOrder.indexOf(dow1) - dayOfWeekOrder.indexOf(dow2)
}