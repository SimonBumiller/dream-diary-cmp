package me.ddcmp.feature.home.entries.personadd

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.bumiller.ddcmp.domain.model.Person
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.domain.usecase.person.SavePersonUseCase
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.ddcmp.feature.home.entries.personadd.PersonAddViewModel.UiEvent
import me.ddcmp.feature.home.entries.personadd.PersonAddViewModel.ViewModelEvent
import me.ddcmp.feature.home.entries.personadd.PersonAddViewModel.ViewModelEvent.CloseDialog
import me.ddcmp.feature.home.navigation.HomeRoutes
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class PersonAddViewModel(
    private val allPersons: AllPersonUseCase,
    private val savePerson: SavePersonUseCase,
    savedStateHandle: SavedStateHandle
) : AppViewModel<UiEvent, ViewModelEvent>() {

    private val navRoute = savedStateHandle.toRoute<HomeRoutes.AddPerson>()

    @OptIn(ExperimentalTime::class)
    override suspend fun handleEvent(event: UiEvent) = when (event) {
        is UiEvent.ChangeName -> updateUiState<PersonState> {
            it.copy(name = event.name)
        }

        is UiEvent.ChangeNote -> updateUiState<PersonState> {
            it.copy(note = event.note)
        }

        UiEvent.Cancel -> fireEvent(CloseDialog)
        UiEvent.Save -> {
            viewModelScope.launch {
                val existing = if (isAdd) null
                else allPersons(AllPersonUseCase.I(id = navRoute.personId))
                    .map {
                        it.singleOrNull()
                    }
                    .firstOrNull()

                val state = uiState<PersonState>().value

                val now = Clock.System.now()

                val person = Person(
                    id = existing?.id ?: 0,
                    created = existing?.created ?: now,
                    edited = now,
                    name = state.name,
                    note = state.note
                )

                savePerson(person)
                fireEvent(CloseDialog)
            }
            Unit
        }
    }

    init {
        registerUiState(PersonState())

        if (navRoute.personId != null) {
            viewModelScope.launch {
                val person = if (isAdd) null
                else allPersons(AllPersonUseCase.I(id = navRoute.personId))
                    .map { it.singleOrNull() }
                    .firstOrNull() ?: return@launch

                updateUiState<PersonState> {
                    val newState = it.copy(
                        name = person?.name ?: "",
                        note = person?.note ?: ""
                    )
                    initialPersonState = newState
                    newState
                }
            }
        }
    }

    /**
     * State of the person attributes.
     */
    val personState = uiState<PersonState>().asStateFlow()

    /**
     * State that holds whether the content can be saved at the moment.
     */
    val canSave = personState
        .map { it != initialPersonState && it.name.isNotBlank() }
        .stateIn(false)

    /**
     * Whether the view is in add mode.
     */
    val isAdd = navRoute.personId == null

    private var initialPersonState = PersonState()

    data class PersonState(
        val name: String = "",
        val note: String = ""
    )

    sealed interface UiEvent : UIE {
        data class ChangeName(val name: String) : UiEvent
        data class ChangeNote(val note: String) : UiEvent
        data object Cancel : UiEvent
        data object Save : UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data object CloseDialog : ViewModelEvent
    }

}