package me.ddcmp.feature.home.entries.dreamdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.bumiller.ddcmp.domain.model.Emotion
import me.bumiller.ddcmp.domain.model.Person
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase.*
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.bumiller.ddcmp.ui.common.biometric.BiometricAuthState
import me.bumiller.ddcmp.ui.common.biometric.BiometricResult
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailViewModel.UiEvent
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailViewModel.ViewModelEvent
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailViewModel.ViewModelEvent.*
import me.ddcmp.feature.home.navigation.HomeRoutes
import kotlin.time.ExperimentalTime

internal class DreamDetailViewModel(
    allDreams: AllDreamUseCase,
    private val saveDream: SaveDreamUseCase,
    savedStateHandle: SavedStateHandle
) : AppViewModel<UiEvent, ViewModelEvent>() {

    private val navRoute = savedStateHandle.toRoute<HomeRoutes.DreamDetail>()

    init {
        registerUiState(UiState())
    }

    /**
     * The state containing the dream to display.
     */
    @OptIn(ExperimentalTime::class)
    val dreamState = allDreams(AllDreamUseCase.I(id = navRoute.dreamId))
        .map { it.singleOrNull() }
        .stateIn(null)

    @OptIn(ExperimentalTime::class)
    override suspend fun handleEvent(event: UiEvent) = when (event) {
        UiEvent.GoToDreamStats -> TODO()
        UiEvent.ToggleFavorite -> viewModelScope.launch {
            val state = dreamState.value
            val newDream = state?.dream?.let {
                it.copy(favorite = !it.favorite)
            }

            if (state != null) {
                val i = I(
                    dream = newDream!!,
                    persons = state.persons,
                    emotions = state.emotions
                )
                saveDream(i)
            }
        }.let { Unit }

        UiEvent.Edit -> fireEvent(GoToEdit(navRoute.dreamId))
        is UiEvent.ClickEmotion -> fireEvent(GoToEmotion(event.emotion.id))
        is UiEvent.ClickPerson -> fireEvent(GoToEmotion(event.person.id))
        UiEvent.Back -> fireEvent(GoBack)
        is UiEvent.OnBiometricAuthResult -> when (event.result) {
            BiometricResult.AuthenticationSuccess -> updateUiState<UiState> { it.copy(authenticated = true) }
            is BiometricResult.AuthenticationError -> fireEvent(GoBack)
            else -> {}
        }

        UiEvent.Unauthenticate -> updateUiState<UiState> { it.copy(authenticated = false) }
        UiEvent.Authenticate -> {
            biometricAuthState.requestAuth()
        }
    }

    val uiState = uiState<UiState>().asStateFlow()

    val biometricAuthState = BiometricAuthState()

    sealed interface UiEvent : UIE {
        data object GoToDreamStats : UiEvent
        data object ToggleFavorite : UiEvent
        data object Edit : UiEvent
        data object Back: UiEvent
        data class ClickEmotion(val emotion: Emotion): UiEvent
        data class ClickPerson(val person: Person): UiEvent
        data class OnBiometricAuthResult(val result: BiometricResult): UiEvent
        data object Unauthenticate: UiEvent
        data object Authenticate: UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data class GoToEdit(val dreamId: Long): ViewModelEvent
        data class GoToEmotion(val emotionId: Long): ViewModelEvent
        data class GoToPerson(val personId: Long): ViewModelEvent
        data object GoBack: ViewModelEvent
    }

    data class UiState(
        val authenticated: Boolean = false
    )

}