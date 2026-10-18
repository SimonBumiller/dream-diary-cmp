package me.ddcmp.feature.home.entries.dreamadd

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.bumiller.ddcmp.domain.model.dream.Confidentiality
import me.bumiller.ddcmp.domain.model.dream.Dream
import me.bumiller.ddcmp.domain.model.emotion.Emotion
import me.bumiller.ddcmp.domain.model.dream.Person
import me.bumiller.ddcmp.domain.model.dream.Tag
import me.bumiller.ddcmp.domain.model.meta.SortMode
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase.I
import me.bumiller.ddcmp.domain.usecase.emotion.AllEmotionsUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.domain.usecase.settings.GetSettingsUseCase
import me.bumiller.ddcmp.domain.usecase.tag.AllTagUseCase
import me.bumiller.ddcmp.domain.util.AppCoroutineScope
import me.bumiller.ddcmp.domain.util.search
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.ddcmp.feature.home.entries.dreamadd.DreamAddViewModel.ViewModelEvent.GoBack
import me.ddcmp.feature.home.navigation.HomeRoutes
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase.I as DreamsQuery

internal class DreamAddViewModel(
    savedStateHandle: SavedStateHandle,
    val allEmotions: AllEmotionsUseCase,
    val allPersons: AllPersonUseCase,
    val allTags: AllTagUseCase,
    val allDreams: AllDreamUseCase,
    val saveDream: SaveDreamUseCase,
    val settings: GetSettingsUseCase,
    val appScope: AppCoroutineScope
) :
    AppViewModel<DreamAddViewModel.UiEvent, DreamAddViewModel.ViewModelEvent>() {

    private var navRoute: HomeRoutes.AddDream = savedStateHandle.toRoute<HomeRoutes.AddDream>()

    override suspend fun handleEvent(event: UiEvent) = when (event) {
        is UiEvent.ChangeDescription -> updateUiState<DreamState> {
            it.copy(description = event.description)
        }

        is UiEvent.ChangeEmotionQuery -> updateUiState<UiState> {
            it.copy(emotionQuery = event.emotionQuery)
        }

        is UiEvent.ChangeNote -> updateUiState<DreamState> {
            it.copy(note = event.note)
        }

        is UiEvent.ChangeTitle -> updateUiState<DreamState> {
            it.copy(title = event.title)
        }

        is UiEvent.ChangePersonQuery -> updateUiState<UiState> {
            it.copy(personQuery = event.personQuery)
        }

        is UiEvent.ChangeTagQuery -> updateUiState<UiState> {
            it.copy(tagQuery = event.tagQuery)
        }

        is UiEvent.SelectEmotion -> {
            updateUiState<DreamState> {
                it.copy(
                    emotions = it.emotions + event.emotion
                )
            }
            updateUiState<UiState> {
                it.copy(
                    emotionQuery = ""
                )
            }
        }

        is UiEvent.UnSelectEmotion -> updateUiState<DreamState> {
            it.copy(
                emotions = it.emotions - event.emotion
            )
        }

        is UiEvent.SelectPerson -> {
            updateUiState<DreamState> {
                it.copy(
                    persons = it.persons + event.person
                )
            }
            updateUiState<UiState> {
                it.copy(
                    emotionQuery = ""
                )
            }
        }

        is UiEvent.UnSelectPerson -> updateUiState<DreamState> {
            it.copy(
                persons = it.persons - event.person
            )
        }

        is UiEvent.Save -> {
            val state = uiState<DreamState>().value

            appScope.launch {
                val existing = if (navRoute.dreamId == null) null
                else allDreams(DreamsQuery(id = navRoute.dreamId))
                    .map { it.singleOrNull() }
                    .firstOrNull()
                val existingDream = existing?.dream

                val now = Clock.System.now()

                val dream = Dream(
                    id = existingDream?.id ?: 0,
                    created = existingDream?.created ?: now,
                    edited = now,
                    dreamt = state.dreamt,
                    title = state.title.ifBlank { null },
                    description = state.description,
                    note = state.note.ifBlank { null },
                    favorite = existingDream?.favorite ?: false,
                    confidentiality = state.confidentiality
                )

                I(
                    dream = dream,
                    persons = state.persons,
                    emotions = state.emotions,
                    tags = state.tags
                ).let { saveDream(it) }
            }

            fireEvent(GoBack)
        }

        is UiEvent.NavigateBack -> {
            val isDialogShown = uiState<UiState>().value.showDiscardDialog
            val didChanges = canSave.value

            // We navigate back either if user gave consent to discard, or if nothing is there to discard
            if (isDialogShown || !didChanges) {
                updateUiState<UiState> {
                    it.copy(showDiscardDialog = false)
                }
                fireEvent(GoBack)
            } else {
                updateUiState<UiState> {
                    it.copy(showDiscardDialog = true)
                }
            }
        }

        is UiEvent.HideDiscardDialog -> updateUiState<UiState> {
            it.copy(showDiscardDialog = false)
        }

        is UiEvent.SetConfidentiality -> updateUiState<DreamState> {
            it.copy(confidentiality = event.confidentiality)
        }

        UiEvent.ToggleDreamtDialog -> updateUiState<UiState> {
            it.copy(showDreamtDialog = !it.showDreamtDialog)
        }

        is UiEvent.ChangeDreamtDate -> updateUiState<DreamState> {
            it.copy(dreamt = event.date)
        }

        is UiEvent.SelectTag -> updateUiState<DreamState> {
            it.copy(tags = it.tags + event.tag)
        }
        is UiEvent.UnselectTag -> updateUiState<DreamState> {
            it.copy(tags = it.tags - event.tag)
        }
    }

    init {
        registerUiState(UiState())
        registerUiState(DreamState())

        val isAdd = navRoute.dreamId == null

        appScope.launch {
            updateUiState<UiState> { it.copy(isEdit = !isAdd) }
            if(isAdd) { // If dream is new, we set the default confidentiality to the one saved in the settings
                val defaultConf = settings(Unit).first().defaultConfidentiality
                updateUiState<DreamState> {
                    it.copy(confidentiality = defaultConf)
                }
                updateUiState<UiState> {
                    it.copy(finishedLoading = true)
                }
            } else { // If the dream exists, we update the state to reflect the existing dream
                val dream = allDreams(AllDreamUseCase.I(id = navRoute.dreamId))
                    .map { it.firstOrNull() }
                    .firstOrNull()

                dream?.let {
                    updateUiState<DreamState> {
                        val newState = it.copy(
                            description = dream.dream.description,
                            title = dream.dream.title ?: "",
                            note = dream.dream.note ?: "",
                            persons = dream.persons,
                            emotions = dream.emotions,
                            tags = dream.tags,
                            confidentiality = dream.dream.confidentiality,
                            dreamt = dream.dream.dreamt
                        )
                        initialDreamState = newState
                        newState
                    }
                }

                updateUiState<UiState> {
                    it.copy(finishedLoading = true)
                }
            }
        }
    }

    private var initialDreamState: DreamState = DreamState()

    /**
     * State that contains all selectable tags.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val tagState =
        uiState<UiState>()
            .combine(uiState<DreamState>()) { uiState, dreamState -> uiState to dreamState }
            .flatMapLatest { (uiState, dreamState) ->
                allTags(AllTagUseCase.I(sortMode = SortMode.TagSort.Name))
                    .map { tags ->
                        tags
                            .map { it.tag }
                            .search(uiState.tagQuery)
                            .filter { person -> dreamState.tags.all { it.id != person.id } }
                    }
            }.stateIn(emptyList())

    /**
     * State that contains all selectable tags.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val personsState =
        uiState<UiState>()
            .combine(uiState<DreamState>()) { uiState, dreamState -> uiState to dreamState }
            .flatMapLatest { (uiState, dreamState) ->
                allPersons(AllPersonUseCase.I())
                    .map { persons ->
                        persons
                            .map { it.person }
                            .search(uiState.personQuery)
                            .filter { person -> dreamState.persons.all { it.id != person.id } }
                    }
            }.stateIn(emptyList())

    /**
     * State that contains all selectable emotions.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val emotionsState = uiState<UiState>()
        .combine(uiState<DreamState>()) { uiState, dreamState -> uiState to dreamState }
        .flatMapLatest { (uiState, dreamState) ->
            allEmotions(AllEmotionsUseCase.I())
                .map { emotions ->
                    emotions
                        .search(uiState.emotionQuery)
                        .filter { emotion -> dreamState.emotions.all { it.id != emotion.id } }
                }
        }.stateIn(emptyList())

    /**
     * The exposed ui state for the composable.
     */
    val uiState = uiState<UiState>().asStateFlow()

    /**
     * The exposed dream state for the composable.
     */
    val dreamState = uiState<DreamState>().asStateFlow()

    /**
     * State that holds whether the current dream state allows saving of the dream.
     * Calculated by checking if the current dream state changed, relative to the initial dream state.
     */
    val canSave = uiState<DreamState>()
        .map { it != initialDreamState && it.description.isNotBlank() }
        .stateIn(false)

    /**
     * State that holds attributes directly related to the dream.
     */
    data class DreamState(

        val description: String = "",

        val title: String = "",

        val note: String = "",

        val persons: List<Person> = emptyList(),

        val emotions: List<Emotion> = emptyList(),

        val tags: List<Tag> = emptyList(),

        val confidentiality: Confidentiality = Confidentiality.None,

        val dreamt: Instant = Clock.System.now()
    )

    /**
     * State that holds other ui-related values.
     */
    data class UiState(

        val isEdit: Boolean = false,

        val personQuery: String = "",

        val emotionQuery: String = "",

        val tagQuery: String = "",

        val showDiscardDialog: Boolean = false,

        val showDreamtDialog: Boolean = false,

        val finishedLoading: Boolean = false

    )

    sealed interface UiEvent : UIE {

        data class ChangeDescription(val description: String) : UiEvent
        data class ChangeNote(val note: String) : UiEvent
        data class ChangeTitle(val title: String) : UiEvent
        data class ChangePersonQuery(val personQuery: String) : UiEvent
        data class ChangeTagQuery(val tagQuery: String) : UiEvent
        data class ChangeEmotionQuery(val emotionQuery: String) : UiEvent
        data class SelectPerson(val person: Person) : UiEvent
        data class SelectTag(val tag: Tag) : UiEvent
        data class UnselectTag(val tag: Tag) : UiEvent
        data class UnSelectPerson(val person: Person) : UiEvent
        data class SelectEmotion(val emotion: Emotion) : UiEvent
        data class UnSelectEmotion(val emotion: Emotion) : UiEvent
        data class SetConfidentiality(val confidentiality: Confidentiality): UiEvent
        data class ChangeDreamtDate(val date: Instant): UiEvent
        data object Save : UiEvent
        data object NavigateBack : UiEvent
        data object HideDiscardDialog : UiEvent
        data object ToggleDreamtDialog: UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data object GoBack : ViewModelEvent
    }

}