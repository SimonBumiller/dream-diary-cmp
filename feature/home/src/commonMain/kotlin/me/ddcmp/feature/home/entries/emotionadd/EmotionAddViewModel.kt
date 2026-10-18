package me.ddcmp.feature.home.entries.emotionadd

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.bumiller.ddcmp.domain.model.emotion.Emotion
import me.bumiller.ddcmp.domain.model.emotion.EmotionCoordinate
import me.bumiller.ddcmp.domain.usecase.emotion.AllEmotionsUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.SaveEmotionUseCase
import me.bumiller.ddcmp.domain.util.AppCoroutineScope
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.ddcmp.feature.home.entries.emotionadd.EmotionAddViewModel.ViewModelEvent.GoBack
import me.ddcmp.feature.home.navigation.HomeRoutes
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class EmotionAddViewModel(
    savedStateHandle: SavedStateHandle,
    val allEmotions: AllEmotionsUseCase,
    val saveEmotion: SaveEmotionUseCase,
    private val appScope: AppCoroutineScope
) :
    AppViewModel<EmotionAddViewModel.UiEvent, EmotionAddViewModel.ViewModelEvent>() {

    private var navRoute: HomeRoutes.AddEmotion = savedStateHandle.toRoute<HomeRoutes.AddEmotion>()

    override suspend fun handleEvent(event: UiEvent) = when (event) {
        is UiEvent.Save -> {
            val state = uiState<EmotionState>().value

            appScope.launch {
                val existingEmotion = if (navRoute.emotionId == null) null
                else allEmotions(AllEmotionsUseCase.I(id = navRoute.emotionId))
                    .map { it.singleOrNull() }
                    .firstOrNull()

                val now = Clock.System.now()
                val coord = state.coordinate!!

                val emotion = Emotion(
                    id = existingEmotion?.id ?: 0,
                    created = existingEmotion?.created ?: now,
                    edited = now,
                    arousal = coord.arousal,
                    valence = coord.valence,
                    name = state.name
                )

                saveEmotion(emotion)
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

        is UiEvent.ChangeCoordinate -> updateUiState<EmotionState> {
            it.copy(coordinate = event.coordinate)
        }

        is UiEvent.ChangeName -> updateUiState<EmotionState> {
            it.copy(name = event.name)
        }
    }

    init {
        registerUiState(UiState())
        registerUiState(EmotionState())

        val isAdd = navRoute.emotionId == null

        appScope.launch {
            if (!isAdd) { // If the emotion exists, we update the state to reflect the existing emotion
                val emotion = allEmotions(AllEmotionsUseCase.I(id = navRoute.emotionId))
                    .map { it.firstOrNull() }
                    .firstOrNull()

                emotion?.let {
                    updateUiState<EmotionState> {
                        val newState = it.copy(
                            name = emotion.name,
                            coordinate = emotion.copy()
                        )
                        initialEmotionState = newState
                        newState
                    }
                }
            }
        }
    }

    private var initialEmotionState: EmotionState = EmotionState()

    /**
     * State that contains all existing emotions.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val emotionsState = allEmotions(AllEmotionsUseCase.I()).stateIn(emptyList())

    /**
     * The exposed ui state for the composable.
     */
    val uiState = uiState<UiState>().asStateFlow()

    /**
     * The exposed emotion state for the composable.
     */
    val emotionState = uiState<EmotionState>().asStateFlow()

    /**
     * The closest emotion to the currently selected one.
     */
    val closest = uiState<EmotionState>().combine(emotionsState) { emotionState, emotions ->
        emotionState.coordinate?.let { coordinate ->
            emotions.minByOrNull { it.distanceTo(coordinate) }
        }
    }.stateIn(null)

    /**
     * State that holds whether the current emotion state allows saving of the emotion.
     * Calculated by checking if the current emotion state changed, relative to the initial emotion state.
     */
    val canSave = uiState<EmotionState>()
        .map { it != initialEmotionState && it.coordinate != null && it.name.isNotBlank() }
        .stateIn(false)

    /**
     * State that holds attributes directly related to the emotion.
     */
    data class EmotionState(

        val coordinate: EmotionCoordinate? = null,

        val name: String = ""
    )

    /**
     * State that holds other ui-related values.
     */
    data class UiState(
        val showDiscardDialog: Boolean = false
    )

    sealed interface UiEvent : UIE {

        data class ChangeCoordinate(val coordinate: EmotionCoordinate) : UiEvent
        data class ChangeName(val name: String) : UiEvent

        data object Save : UiEvent
        data object NavigateBack : UiEvent
        data object HideDiscardDialog : UiEvent

    }

    sealed interface ViewModelEvent : VME {
        data object GoBack : ViewModelEvent
    }

}