package me.ddcmp.feature.stats.entries

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.stats.Stat
import me.bumiller.ddcmp.domain.stats.dream.global.DreamStreakStatistic
import me.bumiller.ddcmp.domain.stats.dream.global.DreamTimeFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.dream.global.DreamWeekdayFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.dream.global.DreamWeeklyFrequencyStatistic
import me.bumiller.ddcmp.domain.usecase.dream.DreamDatesUseCase
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.bumiller.ddcmp.ui.common.dialog.PickedDateRange
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
internal class DreamGlobalViewModel(
    timeFrequency: DreamTimeFrequencyHistogram,
    weekdayFrequency: DreamWeekdayFrequencyHistogram,
    weeklyFrequency: DreamWeeklyFrequencyStatistic,
    dreamStreak: DreamStreakStatistic,
    private val dreamDatesUseCase: DreamDatesUseCase
) : AppViewModel<DreamGlobalViewModel.UiEvent, DreamGlobalViewModel.ViewModelEvent>() {

    private val timeZone = TimeZone.currentSystemDefault()

    init {
        registerUiState(UiState())
    }

    /**
     * The ui state.
     */
    val uiState = uiState<UiState>().asStateFlow()

    private fun firstDreamDate() = dreamDatesUseCase(Unit)
        .map { dates -> dates?.firstDream?.dreamt?.toLocalDateTime(timeZone)?.date }

    private fun <T> dateRangeReactive(reactor: (LocalDateRange) -> Flow<Stat<T>>): Flow<Stat<T>> =
        uiState<UiState>()
            .combine(firstDreamDate()) { uiState, firstDate ->
                if (firstDate == null) return@combine null

                val today = Clock.System.now().toLocalDateTime(timeZone).date

                val start = when (uiState.dateRange) {
                    PickedDateRange.AllTime -> firstDate
                    is PickedDateRange.Offset -> today.minus(uiState.dateRange.offset)
                }

                start..today
            }
            .flatMapLatest {
                // Not enough data is created when we can't create a sensible timespan.
                if (it == null) flowOf(Stat.NotEnoughData())
                else reactor(it)
            }

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
        val query = DreamWeeklyFrequencyStatistic.I(dateRange, dateRange.endInclusive)

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

        UiEvent.ToggleDateRangeDialogVisibility -> updateUiState<UiState> {
            it.copy(showDateRangeDialog = !it.showDateRangeDialog)
        }
    }

    data class UiState(
        val dateRange: PickedDateRange = PickedDateRange.AllTime,
        val showDateRangeDialog: Boolean = false
    )

    sealed interface UiEvent : UIE {
        data class ChangeDateRange(val dateRange: PickedDateRange) : UiEvent
        data object Return : UiEvent
        data object ToggleDateRangeDialogVisibility : UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data object Return : ViewModelEvent
    }

}