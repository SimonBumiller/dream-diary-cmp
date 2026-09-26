package me.ddcmp.feature.home.entries.persondetail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.model.Emotion
import me.bumiller.ddcmp.domain.model.Person
import me.bumiller.ddcmp.domain.model.meta.SortDirection
import me.bumiller.ddcmp.domain.model.meta.SortMode
import me.bumiller.ddcmp.domain.stats.person.PersonInfluencingEmotionsStatistic
import me.bumiller.ddcmp.domain.stats.person.PersonInfluencingPersonsStatistic
import me.bumiller.ddcmp.domain.stats.person.PersonOccurrenceStatistic
import me.bumiller.ddcmp.domain.stats.person.PersonTimeFrequencyHistogram
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.ddcmp.feature.home.entries.persondetail.PersonDetailViewModel.UiEvent
import me.ddcmp.feature.home.entries.persondetail.PersonDetailViewModel.ViewModelEvent
import me.ddcmp.feature.home.entries.persondetail.PersonDetailViewModel.ViewModelEvent.Edit
import me.ddcmp.feature.home.entries.persondetail.PersonDetailViewModel.ViewModelEvent.GoBack
import me.ddcmp.feature.home.entries.persondetail.PersonDetailViewModel.ViewModelEvent.GoToDream
import me.ddcmp.feature.home.entries.persondetail.PersonDetailViewModel.ViewModelEvent.GoToEmotion
import me.ddcmp.feature.home.entries.persondetail.PersonDetailViewModel.ViewModelEvent.GoToPage
import me.ddcmp.feature.home.entries.persondetail.PersonDetailViewModel.ViewModelEvent.GoToPerson
import me.ddcmp.feature.home.navigation.HomeRoutes
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class PersonDetailViewModel(
    allPersons: AllPersonUseCase,
    allDreams: AllDreamUseCase,
    savedStateHandle: SavedStateHandle,
    associatedEmotions: PersonInfluencingEmotionsStatistic,
    associatedPersons: PersonInfluencingPersonsStatistic,
    personOccurrence: PersonOccurrenceStatistic,
    timeFrequency: PersonTimeFrequencyHistogram
) : AppViewModel<UiEvent, ViewModelEvent>() {

    private val personId = savedStateHandle.toRoute<HomeRoutes.PersonDetail>().personId

    /**
     * The state containing the person to display.
     */
    @OptIn(ExperimentalTime::class)
    val personState = allPersons(AllPersonUseCase.I(id = personId))
        .map { it.singleOrNull() }
        .stateIn(null)

    private val dreamsQuery = AllDreamUseCase.I(
        sortMode = SortMode.DreamSort.Dreamt,
        sortDirection = SortDirection.Descending,
        personIds = setOf(personId)
    )

    init {
        registerUiState(UiState())
    }

    /**
     * The ui state
     */
    val uiState = uiState<UiState>()
        .asStateFlow()

    /**
     * State containing all the dreams in which the person occurs.
     */
    val dreamsState = allDreams(dreamsQuery)
        .stateIn(null)

    /**
     * State containing the associated emotions.
     */
    val associatedEmotions = associatedEmotions(personId)
        .map { stat ->
            stat
                .map { it.take(5) }
                .notEnoughDataIf { it.isEmpty() }
        }
        .stateIn(null)

    /**
     * Stat histogram for the frequency of the person.
     */
    val timeFrequency = timeFrequency(personId)
        .stateIn(null)

    /**
     * State containing the associated persons.
     */
    val associatedPersons = associatedPersons(personId)
        .map { stat ->
            stat
                .map { it.take(5) }
                .notEnoughDataIf { it.isEmpty() }
        }
        .stateIn(null)

        override suspend fun handleEvent(event: UiEvent) = when (event) {
            UiEvent.Edit -> fireEvent(Edit(personId))
            is UiEvent.GoToDream -> fireEvent(GoToDream(event.dreamId))
            UiEvent.GoBack -> fireEvent(GoBack)
            is UiEvent.ClickEmotion -> fireEvent(GoToEmotion(event.emotion.id))
            is UiEvent.ClickPerson -> fireEvent(GoToPerson(event.person.id))
            is UiEvent.ChangeTab -> {
                fireEvent(GoToPage(event.tab.index))
                updateUiState<UiState> {
                    it.copy(selectedTab = event.tab)
                }
            }
        }

    val occurrenceStats = personOccurrence(personId)
        .stateIn(null)

    data class UiState(
        val selectedTab: PersonDetailTabs = PersonDetailTabs.Stats
    )

    sealed interface UiEvent : UIE {
        data class GoToDream(val dreamId: Long) : UiEvent
        data class ClickPerson(val person: Person) : UiEvent
        data class ClickEmotion(val emotion: Emotion) : UiEvent
        data class ChangeTab(val tab: PersonDetailTabs) : UiEvent
        data object Edit : UiEvent
        data object GoBack : UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data class GoToDream(val dreamId: Long) : ViewModelEvent
        data class GoToPerson(val personId: Long) : ViewModelEvent
        data class GoToEmotion(val emotionId: Long) : ViewModelEvent
        data class Edit(val personId: Long) : ViewModelEvent
        data class GoToPage(val page: Int) : ViewModelEvent
        data object GoBack : ViewModelEvent
    }

}