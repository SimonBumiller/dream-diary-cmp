package me.ddcmp.feature.stats.domain

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateRange
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.stats.Stat
import me.bumiller.ddcmp.domain.usecase.dream.DreamDatesUseCase
import me.bumiller.ddcmp.domain.usecase.settings.GetSettingsUseCase
import me.bumiller.ddcmp.domain.usecase.settings.SetSettingsUseCase
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.bumiller.ddcmp.ui.common.dialog.PickedDateRange
import me.bumiller.ddcmp.ui.common.dialog.PreconfiguredDateRanges
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Abstract class that models a ViewModel that also manages a date range ui state.
 */
internal abstract class DateRangeViewModel<UiEvent : UIE, ViewModelEvent : VME>(
    private val dreamDates: DreamDatesUseCase,
    private val getSettings: GetSettingsUseCase,
    private val setSettings: SetSettingsUseCase
) : AppViewModel<UiEvent, ViewModelEvent>() {

    protected val timeZone = TimeZone.currentSystemDefault()

    init {
        val uiState = registerUiState(DateRangeUiState())

        viewModelScope.launch {
            val settings = getSettings(Unit).first()

            val preconfiguredDateRange = PreconfiguredDateRanges
                .find { it.second == settings.statisticsDateRange }
                ?.second

            val pickedDateRange = when (preconfiguredDateRange) {
                null -> PickedDateRange.AllTime
                else -> PickedDateRange.Offset(preconfiguredDateRange)
            }
            uiState.update {
                it.copy(dateRange = pickedDateRange)
            }
        }
    }

    val dateRangeState = uiState<DateRangeUiState>()

    /**
     * Updates the date range state.
     */
    protected fun updateDateRange(range: PickedDateRange) {
        updateUiState<DateRangeUiState> {
            it.copy(dateRange = range)
        }

        viewModelScope.launch {
            val settings = getSettings(Unit).first()
            val updated = settings.copy(
                statisticsDateRange = when (range) {
                    PickedDateRange.AllTime -> null
                    is PickedDateRange.Offset -> range.offset
                }
            )
            setSettings(updated)
        }
    }

    /**
     * Updates the date range state.
     */
    protected fun toggleDateRangeDialogVisibility() = updateUiState<DateRangeUiState> {
        it.copy(showDateRangeDialog = !it.showDateRangeDialog)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun <T> dateRangeReactive(reactor: (LocalDateRange) -> Flow<Stat<T>>): Flow<Stat<T>> =
        uiState<DateRangeUiState>()
            .combine(firstDreamDate()) { uiState, firstDate ->
                if (firstDate == null || uiState.dateRange == null) return@combine null

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

    private fun firstDreamDate() = dreamDates(Unit)
        .map { dates -> dates?.firstDream?.dreamt?.toLocalDateTime(timeZone)?.date }

    /**
     * The date range ui state.
     */
    data class DateRangeUiState(

        /**
         * The picked date range of the dialog.
         */
        val dateRange: PickedDateRange? = PickedDateRange.AllTime,

        /**
         * Whether the dialog should be shown.
         */
        val showDateRangeDialog: Boolean = false
    )

}