package me.ddcmp.feature.stats.entries.dreamglobal

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.stats.dream.global.DreamStreakStatistic
import me.bumiller.ddcmp.domain.stats.dream.global.DreamTimeFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.dream.global.DreamWeekdayFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.dream.global.DreamWeeklyFrequencyStatistic
import me.bumiller.ddcmp.domain.stats.person.PersonsOccurrencesCountStatistic
import me.bumiller.ddcmp.domain.usecase.dream.DreamDatesUseCase
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.bumiller.ddcmp.ui.common.dialog.PickedDateRange
import me.ddcmp.feature.stats.domain.DateRangeViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
internal class DreamGlobalViewModel(
    timeFrequency: DreamTimeFrequencyHistogram,
    weekdayFrequency: DreamWeekdayFrequencyHistogram,
    weeklyFrequency: DreamWeeklyFrequencyStatistic,
    dreamStreak: DreamStreakStatistic,
    dreamDates: DreamDatesUseCase,
    personsOccurrences: PersonsOccurrencesCountStatistic
) : DateRangeViewModel<DreamGlobalViewModel.UiEvent, DreamGlobalViewModel.ViewModelEvent>(dreamDates) {

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
     * Contains the top five most occurring persons together with their occurrence count.
     */
    val topOccurringPersons = dateRangeReactive { dateRange ->
        val input = PersonsOccurrencesCountStatistic.I(dateRange, 5)
        personsOccurrences(input)
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
        is UiEvent.ChangeDateRange -> updateDateRange(event.dateRange)
        UiEvent.ToggleDateRangeDialogVisibility -> toggleDateRangeDialogVisibility()
        is UiEvent.ClickOnPerson -> fireEvent(ViewModelEvent.GoToPerson(event.personId))
    }

    sealed interface UiEvent : UIE {
        data class ChangeDateRange(val dateRange: PickedDateRange) : UiEvent
        data class ClickOnPerson(val personId: Long): UiEvent
        data object Return : UiEvent
        data object ToggleDateRangeDialogVisibility : UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data class GoToPerson(val personId: Long): ViewModelEvent
        data object Return : ViewModelEvent
    }

}