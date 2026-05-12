package me.ddcmp.feature.home.entries.dreamlist

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.model.Emotion
import me.bumiller.ddcmp.domain.model.Person
import me.bumiller.ddcmp.domain.model.meta.SortDirection
import me.bumiller.ddcmp.domain.model.meta.SortMode.DreamSort
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class DreamListViewModel(

    private val getDreams: AllDreamUseCase,

    private val saveDream: SaveDreamUseCase

) : AppViewModel<DreamListViewModel.UiEvent, DreamListViewModel.ViewModelEvent>() {

    init {
        registerUiState(UiState())
    }

    /**
     * The state that contains the dreams.
     */
    val dreamState = createDreamsState()
        .stateIn(emptyList())

    /**
     * The ui state
     */
    val uiState = uiState<UiState>().asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
    private fun createDreamsState(): Flow<List<DreamAggregate>> = uiState<UiState>()
        .flatMapLatest { uiState ->
            val i = AllDreamUseCase.I(
                sortMode = uiState.sortMode,
                sortDirection = uiState.sortDirection
            )

            getDreams(i).map { os ->
                os.map(DreamAggregate::create)
            }
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

        is UiEvent.ClickDream -> TODO()
        UiEvent.GoToSettings -> fireEvent(ViewModelEvent.GoToSettings)
        UiEvent.GoToStats -> fireEvent(ViewModelEvent.GoToStats)
        UiEvent.GoToEmotions -> fireEvent(ViewModelEvent.GoToEmotions)
        UiEvent.AddDream -> fireEvent(ViewModelEvent.GoToAddDream)
        UiEvent.GoToPersons -> fireEvent(ViewModelEvent.GoToPersons)
    }

    sealed interface UiEvent : UIE {

        data class ToggleDropdownVisibility(val visible: Boolean) : UiEvent

        data class ChangeSortMode(val mode: DreamSort) : UiEvent

        data class ChangeSortDirection(val direction: SortDirection) : UiEvent

        data object GoToSettings : UiEvent

        data object GoToStats : UiEvent

        data object GoToEmotions : UiEvent

        data object GoToPersons : UiEvent

        data class ClickDream(val dream: Dream) : UiEvent

        data object AddDream: UiEvent

    }

    sealed interface ViewModelEvent : VME {

        data object GoToAddDream: ViewModelEvent

        data object GoToEmotions: ViewModelEvent
        data object GoToSettings: ViewModelEvent
        data object GoToStats: ViewModelEvent
        data object GoToPersons: ViewModelEvent

    }

    data class UiState(

        val sortModeDropdownVisible: Boolean = false,

        val sortMode: DreamSort = DreamSort.Dreamt,

        val sortDirection: SortDirection = SortDirection.Descending,

        val selectedDream: Dream? = null

    )

    data class DreamAggregate(

        val dream: Dream,

        val emotions: List<Emotion>,

        val persons: List<Person>

    ) {

        companion object {

            fun create(o: AllDreamUseCase.O) = DreamAggregate(o.dream, o.emotions, o.persons)

        }

    }

}