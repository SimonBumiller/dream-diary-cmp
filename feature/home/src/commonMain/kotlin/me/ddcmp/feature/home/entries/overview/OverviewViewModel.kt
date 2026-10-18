package me.ddcmp.feature.home.entries.overview

import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.dream.Dream
import me.bumiller.ddcmp.domain.model.meta.SortDirection
import me.bumiller.ddcmp.domain.stats.dream.global.DreamWeeklyFrequencyStatistic
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.AllEmotionsUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.ddcmp.feature.home.entries.overview.OverviewViewModel.UiEvent
import me.ddcmp.feature.home.entries.overview.OverviewViewModel.ViewModelEvent
import me.ddcmp.feature.home.entries.overview.OverviewViewModel.ViewModelEvent.AddDream
import me.ddcmp.feature.home.entries.overview.OverviewViewModel.ViewModelEvent.GoToDream
import me.ddcmp.feature.home.entries.overview.OverviewViewModel.ViewModelEvent.GoToDreams
import me.ddcmp.feature.home.entries.overview.OverviewViewModel.ViewModelEvent.GoToEmotions
import me.ddcmp.feature.home.entries.overview.OverviewViewModel.ViewModelEvent.GoToPersons
import me.ddcmp.feature.home.entries.overview.OverviewViewModel.ViewModelEvent.GoToSettings
import me.ddcmp.feature.home.entries.overview.OverviewViewModel.ViewModelEvent.GoToStatistics
import me.ddcmp.feature.home.entries.overview.OverviewViewModel.ViewModelEvent.GoToTags
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal class OverviewViewModel(
    allDreams: AllDreamUseCase,
    allPersons: AllPersonUseCase,
    allEmotions: AllEmotionsUseCase,
    weeklyFreqStat: DreamWeeklyFrequencyStatistic
): AppViewModel<UiEvent, ViewModelEvent>() {

    override suspend fun handleEvent(event: UiEvent) = when (event) {
        is UiEvent.GoToDream -> fireEvent(GoToDream(event.dream.id, event.dream.confidentiality.locked))
        UiEvent.GoToDreams -> fireEvent(GoToDreams)
        UiEvent.GoToEmotions -> fireEvent(GoToEmotions)
        UiEvent.GoToPersons -> fireEvent(GoToPersons)
        UiEvent.GoToSettings -> fireEvent(GoToSettings)
        UiEvent.GoToStatistics -> fireEvent(GoToStatistics)
        UiEvent.AddDream -> fireEvent(AddDream)
        UiEvent.GoToTags -> fireEvent(GoToTags)
    }

    private val recentDreamsQuery = AllDreamUseCase.I(
        sortDirection = SortDirection.Descending,
        timeRange = Clock.System.now().minus(7.days)..Instant.DISTANT_FUTURE
    )
    val recentDreams = allDreams(recentDreamsQuery)
        .stateIn(initial = null)

    val personCount = allPersons(AllPersonUseCase.I())
        .map { it.size }
        .stateIn(null)

    val emotionCount = allEmotions(AllEmotionsUseCase.I())
        .map { it.size }
        .stateIn(null)

    val dreamCount = allDreams(AllDreamUseCase.I())
        .map { it.size }
        .stateIn(null)


    private fun weeklyFreqStatQuery(): DreamWeeklyFrequencyStatistic.I {
        val zone = TimeZone.currentSystemDefault()
        val start = Instant.DISTANT_PAST.toLocalDateTime(zone).date
        val end = Clock.System.now().toLocalDateTime(zone).date

        return DreamWeeklyFrequencyStatistic.I(
            dateRange = start..end,
            referenceDay = end
        )
    }
    val recentDreamActivity = weeklyFreqStat(weeklyFreqStatQuery())
        .map { o -> o.map { it.lastSevenDaysRelative } }
        .stateIn(null)


    sealed interface UiEvent: UIE {
        data object GoToPersons: UiEvent
        data object GoToTags: UiEvent
        data object GoToSettings: UiEvent
        data object GoToEmotions: UiEvent
        data object GoToDreams: UiEvent
        data object GoToStatistics: UiEvent
        data object AddDream : UiEvent

        data class GoToDream(val dream: Dream): UiEvent
    }

    sealed interface ViewModelEvent: VME {
        data object GoToPersons: ViewModelEvent
        data object GoToSettings: ViewModelEvent
        data object GoToEmotions: ViewModelEvent
        data object GoToDreams: ViewModelEvent
        data object GoToTags: ViewModelEvent
        data object GoToStatistics: ViewModelEvent
        data class GoToDream(val dreamId: Long, val secured: Boolean): ViewModelEvent
        data object AddDream : ViewModelEvent
    }

}