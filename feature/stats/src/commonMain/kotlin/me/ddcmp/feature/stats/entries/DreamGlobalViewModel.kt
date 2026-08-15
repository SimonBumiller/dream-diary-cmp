package me.ddcmp.feature.stats.entries

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.LocalDateRange
import me.bumiller.ddcmp.domain.model.stats.Stat
import me.bumiller.ddcmp.domain.stats.dream.global.DreamStreakStatistic
import me.bumiller.ddcmp.domain.stats.dream.global.DreamTimeFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.dream.global.DreamWeekdayFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.dream.global.DreamWeeklyFrequencyStatistic
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
internal class DreamGlobalViewModel(
    timeFrequency: DreamTimeFrequencyHistogram,
    weekdayFrequency: DreamWeekdayFrequencyHistogram,
    weeklyFrequency: DreamWeeklyFrequencyStatistic,
    dreamStreak: DreamStreakStatistic
) : AppViewModel<DreamGlobalViewModel.UiEvent, DreamGlobalViewModel.ViewModelEvent>() {

    private val timeZone = TimeZone.currentSystemDefault()

    init {
        registerUiState(UiState())
    }

    private fun dateRange() = uiState<UiState>().map { it.dateRange }

    private fun <T> dateRangeReactive(reactor: (LocalDateRange) -> Flow<T>): Flow<T> =
        uiState<UiState>().flatMapLatest { state -> reactor(state.dateRange) }

    /**
     * The time vs dream frequency histogram.
     */
    val timeHistogram = dateRangeReactive { dateRange ->
        timeFrequency(dateRange)
    }.stateIn(null)

    /**
     * The weekday vs dream frequency histogram.
     */
    val weekdayHistogram = dateRangeReactive { dateRange ->
        weekdayFrequency(dateRange)
    }.stateIn(null)

    /**
     * The statistic about weekly average dreams
     */
    val weeklyFrequency = dateRangeReactive { dateRange ->
        val today = Clock.System.now().toLocalDateTime(timeZone).date
        val query = DreamWeeklyFrequencyStatistic.I(dateRange, today)

        weeklyFrequency(query)
    }.stateIn(null)

    /**
     * The streak statistics, i.e. longest and current streak.
     */
    val streakStatistic = dateRangeReactive { dateRange ->
        val today = Clock.System.now().toLocalDateTime(timeZone).date
        val query = DreamStreakStatistic.I(dateRange, today)

        dreamStreak(query)
    }.stateIn(null)

    override suspend fun handleEvent(event: UiEvent) = when (event) {
        UiEvent.Return -> fireEvent(ViewModelEvent.Return)
        is UiEvent.ChangeDateRange -> updateUiState<UiState> {
            it.copy(dateRange = event.dateRange)
        }
    }

    data class UiState(
        val dateRange: LocalDateRange = LocalDateRange.Limitless
    )

    sealed interface UiEvent : UIE {
        data class ChangeDateRange(val dateRange: LocalDateRange) : UiEvent
        data object Return : UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data object Return : ViewModelEvent
    }

}