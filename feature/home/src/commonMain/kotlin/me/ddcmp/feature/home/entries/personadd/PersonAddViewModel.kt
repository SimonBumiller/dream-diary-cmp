package me.ddcmp.feature.home.entries.personadd

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.bumiller.ddcmp.domain.model.dream.Person
import me.bumiller.ddcmp.domain.model.dream.Tag
import me.bumiller.ddcmp.domain.model.meta.SortMode
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.domain.usecase.person.SavePersonUseCase
import me.bumiller.ddcmp.domain.usecase.tag.AllTagUseCase
import me.bumiller.ddcmp.domain.util.AppCoroutineScope
import me.bumiller.ddcmp.domain.util.search
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
    private val allTags: AllTagUseCase,
    private val appScope: AppCoroutineScope,
    savedStateHandle: SavedStateHandle
) : AppViewModel<UiEvent, ViewModelEvent>() {

    private val navRoute = savedStateHandle.toRoute<HomeRoutes.AddPerson>()

    init {
        registerUiState(PersonState())
        registerUiState(UiState())

        if (navRoute.personId != null) {
            viewModelScope.launch {
                val person = if (isAdd) null
                else allPersons(AllPersonUseCase.I(id = navRoute.personId))
                    .map { it.singleOrNull() }
                    .firstOrNull() ?: return@launch

                updateUiState<PersonState> {
                    val newState = it.copy(
                        name = person?.person?.name ?: "",
                        note = person?.person?.note ?: "",
                        tags = person?.tags ?: emptyList()
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

    val uiState = uiState<UiState>().asStateFlow()

    private val tagsQuery = AllTagUseCase.I(sortMode = SortMode.TagSort.Name)

    /**
     * The tags available for selection by the user.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val selectableTags = uiState<UiState>()
        .combine(uiState<PersonState>()) { uiState, personState -> uiState to personState }
        .flatMapLatest { (uiState, personState) ->
            allTags(tagsQuery)
                .map { tags ->
                    tags.map(AllTagUseCase.O::tag)
                        .search(uiState.tagQuery)
                        .filter { it.id !in personState.tags.map(Tag::id) }
                }
        }.stateIn(emptyList())

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
            appScope.launch {
                val existing = if (isAdd) null
                else allPersons(AllPersonUseCase.I(id = navRoute.personId))
                    .map {
                        it.singleOrNull()
                    }
                    .firstOrNull()

                val state = uiState<PersonState>().value

                val now = Clock.System.now()

                val person = Person(
                    id = existing?.person?.id ?: 0,
                    created = existing?.person?.created ?: now,
                    edited = now,
                    name = state.name,
                    note = state.note
                )

                val saveInput = SavePersonUseCase.I(person, state.tags)
                savePerson(saveInput)
                fireEvent(CloseDialog)
            }
            Unit
        }

        is UiEvent.AddTag -> updateUiState<PersonState> {
            it.copy(tags = it.tags + event.tag)
        }

        is UiEvent.RemoveTag -> updateUiState<PersonState> {
            it.copy(tags = it.tags.filterNot { tag -> tag.id == event.tagId })
        }

        is UiEvent.ChangeTagQuery -> updateUiState<UiState> {
            it.copy(tagQuery = event.query)
        }
    }

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
        val note: String = "",
        val tags: List<Tag> = emptyList()
    )

    data class UiState(
        val tagQuery: String = ""
    )

    sealed interface UiEvent : UIE {
        data class ChangeName(val name: String) : UiEvent
        data class ChangeNote(val note: String) : UiEvent
        data class ChangeTagQuery(val query: String) : UiEvent
        data class AddTag(val tag: Tag) : UiEvent
        data class RemoveTag(val tagId: Long) : UiEvent
        data object Cancel : UiEvent
        data object Save : UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data object CloseDialog : ViewModelEvent
    }

}