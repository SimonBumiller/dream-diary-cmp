package me.ddcmp.feature.home.entries.personlist

import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.ddcmp.feature.home.entries.personlist.PersonListViewModel.UiEvent
import me.ddcmp.feature.home.entries.personlist.PersonListViewModel.ViewModelEvent
import me.ddcmp.feature.home.entries.personlist.PersonListViewModel.ViewModelEvent.NavigateBack
import me.ddcmp.feature.home.entries.personlist.PersonListViewModel.ViewModelEvent.NavigateToAdd
import me.ddcmp.feature.home.entries.personlist.PersonListViewModel.ViewModelEvent.NavigateToEdit
import me.ddcmp.feature.home.entries.personlist.PersonListViewModel.ViewModelEvent.NavigateToPerson
import kotlin.time.ExperimentalTime

internal class PersonListViewModel(
    getPersons: AllPersonUseCase
) :
    AppViewModel<UiEvent, ViewModelEvent>() {

    @OptIn(ExperimentalTime::class)
    override suspend fun handleEvent(event: UiEvent) = when (event) {

        UiEvent.AddPerson -> fireEvent(NavigateToAdd)
        UiEvent.GoBack -> fireEvent(NavigateBack)
        is UiEvent.EditPerson -> fireEvent(NavigateToEdit(event.id))
        is UiEvent.GoToPerson -> fireEvent(NavigateToPerson(event.id))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val persons = getPersons(AllPersonUseCase.I())
        .stateIn(emptyList())

    sealed interface UiEvent : UIE {
        data object GoBack : UiEvent
        data object AddPerson : UiEvent
        data class EditPerson(val id: Long) : UiEvent
        data class GoToPerson(val id: Long) : UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data object NavigateBack : ViewModelEvent
        data object NavigateToAdd : ViewModelEvent
        data class NavigateToEdit(val id: Long) : ViewModelEvent
        data class NavigateToPerson(val id: Long) : ViewModelEvent
    }

}