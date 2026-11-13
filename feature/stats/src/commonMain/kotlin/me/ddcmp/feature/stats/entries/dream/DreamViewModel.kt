package me.ddcmp.feature.stats.entries.dream

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.stats.dream.global.MarkedDreamLengthFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.dream.global.MarkedDreamPersonCountFrequencyHistogram
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.dream.DreamDatesUseCase
import me.bumiller.ddcmp.domain.usecase.settings.GetSettingsUseCase
import me.bumiller.ddcmp.domain.usecase.settings.SetSettingsUseCase
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.ddcmp.feature.stats.domain.DateRangeViewModel
import me.ddcmp.feature.stats.entries.dream.DreamViewModel.UiEvent
import me.ddcmp.feature.stats.entries.dream.DreamViewModel.ViewModelEvent
import me.ddcmp.feature.stats.navigation.StatsRoutes
import kotlin.time.ExperimentalTime

internal class DreamViewModel(
    lengthHistogramUc: MarkedDreamLengthFrequencyHistogram,
    personCountHistogramUc: MarkedDreamPersonCountFrequencyHistogram,
    dreamDates: DreamDatesUseCase,
    savedStateHandle: SavedStateHandle,
    allDreams: AllDreamUseCase,
    getSettings: GetSettingsUseCase,
    setSettings: SetSettingsUseCase
) : DateRangeViewModel<UiEvent, ViewModelEvent>(dreamDates, getSettings, setSettings) {

    private val dreamId = savedStateHandle.toRoute<StatsRoutes.Dream>().id

    /**
     * The dream state.
     */
    val dream = allDreams(dreamQuery())
        .map { it.singleOrNull() }
        .stateIn(null)

    /**
     * The histogram for the dream lengths.
     */
    val lengthHistogram = dateRangeReactive {
        val query = MarkedDreamLengthFrequencyHistogram.I(it, dreamId)
        lengthHistogramUc(query)
    }
        .stateIn(null)

    /**
     * The histogram for the people per dream.
     */
    val personCountHistogram = dateRangeReactive {
        val query = MarkedDreamPersonCountFrequencyHistogram.I(it, dreamId)
        personCountHistogramUc(query)
    }.stateIn(null)

    override suspend fun handleEvent(event: UiEvent) = when (event) {
        UiEvent.OnBack -> fireEvent(ViewModelEvent.OnBack)
    }

    sealed interface UiEvent : UIE {
        data object OnBack : UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data object OnBack : ViewModelEvent
    }

    @OptIn(ExperimentalTime::class)
    private fun dreamQuery() = AllDreamUseCase.I(id = dreamId)

}