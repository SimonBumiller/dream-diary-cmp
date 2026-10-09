package me.ddcmp.feature.home.entries.dreamlist

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.dream.Dream
import me.bumiller.ddcmp.domain.model.meta.SortDirection
import me.bumiller.ddcmp.domain.model.meta.SortMode.DreamSort
import me.bumiller.ddcmp.domain.stats.dream.global.DreamWeeklyFrequencyStatistic
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.ddcmp.feature.home.entries.dreamlist.DreamListViewModel.ViewModelEvent.GoToAddDream
import me.ddcmp.feature.home.entries.dreamlist.DreamListViewModel.ViewModelEvent.GoToDreamDetail
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class DreamListViewModel(

    private val getDreams: AllDreamUseCase

) : AppViewModel<DreamListViewModel.UiEvent, DreamListViewModel.ViewModelEvent>() {

    init {
        registerUiState(UiState())
    }

    /**
     * The state that contains the dreams.
     */
    val dreamState = createDreamsState()
        .stateIn(null)

    /**
     * The ui state
     */
    val uiState = uiState<UiState>().asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
    private fun createDreamsState(): Flow<List<AllDreamUseCase.O>> = uiState<UiState>()
        .flatMapLatest { uiState ->
            val i = AllDreamUseCase.I(
                sortMode = uiState.sortMode,
                sortDirection = uiState.sortDirection
            )

            getDreams(i)
        }

    override suspend fun handleEvent(event: UiEvent) = when (event) {
        is UiEvent.ChangeSortDirection -> {
            updateUiState<UiState> {
                it.copy(sortDirection = event.direction)
            }
        }

        is UiEvent.ChangeSortMode -> {
            updateUiState<UiState> {
                it.copy(sortMode = event.mode)
            }
        }

        is UiEvent.ToggleDropdownVisibility -> {
            updateUiState<UiState> {
                it.copy(sortModeDropdownVisible = event.visible)
            }
        }

        is UiEvent.ClickDream -> fireEvent(GoToDreamDetail(event.dream.id))
        UiEvent.AddDream -> fireEvent(GoToAddDream)
        UiEvent.GoBack -> fireEvent(ViewModelEvent.GoBack)
    }

    sealed interface UiEvent : UIE {

        data class ToggleDropdownVisibility(val visible: Boolean) : UiEvent

        data class ChangeSortMode(val mode: DreamSort) : UiEvent

        data class ChangeSortDirection(val direction: SortDirection) : UiEvent

        data class ClickDream(val dream: Dream) : UiEvent

        data object AddDream : UiEvent
        data object GoBack : UiEvent

    }

    sealed interface ViewModelEvent : VME {

        data object GoToAddDream : ViewModelEvent
        data object GoBack : ViewModelEvent

        data class GoToDreamDetail(val dreamId: Long) : ViewModelEvent

    }

    data class UiState(

        val sortModeDropdownVisible: Boolean = false,

        val sortMode: DreamSort = DreamSort.Dreamt,

        val sortDirection: SortDirection = SortDirection.Ascending,

        val selectedDream: Dream? = null

    )

}