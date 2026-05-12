package me.ddcmp.feature.home.entries.emotionlist

import dream_diary_cmp.feature.home.generated.resources.Res
import dream_diary_cmp.feature.home.generated.resources.basic_emotions_arousal
import dream_diary_cmp.feature.home.generated.resources.basic_emotions_names
import dream_diary_cmp.feature.home.generated.resources.basic_emotions_valences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asStateFlow
import me.bumiller.ddcmp.domain.model.Arousal
import me.bumiller.ddcmp.domain.model.Emotion
import me.bumiller.ddcmp.domain.model.Valence
import me.bumiller.ddcmp.domain.usecase.emotion.AllEmotionsUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.SaveEmotionUseCase
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.ddcmp.feature.home.entries.emotionlist.EmotionListViewModel.UiEvent
import me.ddcmp.feature.home.entries.emotionlist.EmotionListViewModel.ViewModelEvent
import org.jetbrains.compose.resources.getStringArray
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class EmotionListViewModel(
    getEmotions: AllEmotionsUseCase,
    private val saveEmotion: SaveEmotionUseCase
) :
    AppViewModel<UiEvent, ViewModelEvent>() {

    init {
        registerUiState<UiState>(UiState())
    }

    val uiState = uiState<UiState>().asStateFlow()

    @OptIn(ExperimentalTime::class)
    override suspend fun handleEvent(event: UiEvent) = when (event) {
        is UiEvent.SelectEmotion -> updateUiState<UiState> {
            it.copy(selectedEmotion = event.emotion)
        }

        UiEvent.AddEmotion -> fireEvent(ViewModelEvent.NavigateToAdd)
        UiEvent.GoBack -> fireEvent(ViewModelEvent.NavigateBack)
        UiEvent.AddBasicEmotions -> {
            val names = getStringArray(Res.array.basic_emotions_names)
            val valences = getStringArray(Res.array.basic_emotions_valences)
                .map(String::toFloat)
            val arousals = getStringArray(Res.array.basic_emotions_arousal)
                .map(String::toFloat)

            val now = Clock.System.now()

            val basicEmotions = names.indices.map { index ->
                Emotion(
                    id = 0,
                    created = now,
                    edited = now,
                    arousal = Arousal(arousals[index]),
                    valence = Valence(valences[index]),
                    name = names[index]
                )
            }

            basicEmotions.forEach { saveEmotion(it) }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val emotions = getEmotions(AllEmotionsUseCase.I())
        .stateIn(emptyList())

    data class UiState(
        val selectedEmotion: Emotion? = null
    )

    sealed interface UiEvent : UIE {
        data object GoBack : UiEvent
        data object AddEmotion : UiEvent
        data object AddBasicEmotions : UiEvent
        data class SelectEmotion(val emotion: Emotion?) : UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data object NavigateBack : ViewModelEvent
        data object NavigateToAdd : ViewModelEvent
    }

}