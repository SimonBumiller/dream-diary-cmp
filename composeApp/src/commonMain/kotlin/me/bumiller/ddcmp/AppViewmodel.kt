package me.bumiller.ddcmp

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.bumiller.ddcmp.domain.model.settings.Settings
import me.bumiller.ddcmp.domain.usecase.settings.GetSettingsUseCase
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.bumiller.ddcmp.ui.common.AppViewModel as BaseViewModel

/**
 * Viewmodel that controls the top-level app functions.
 */
internal class AppViewmodel(

    /**
     * The usecase to get the settings.
     */
    getSettings: GetSettingsUseCase

) : BaseViewModel<AppViewmodel.UiEvent, AppViewmodel.ViewModelEvent>() {

    internal interface UiEvent: UIE
    internal interface ViewModelEvent: VME

    private val _initialEntry = MutableStateFlow<Boolean?>(null)
    val doOnboarding = _initialEntry.asStateFlow()

    init {
        viewModelScope.launch {
            val settings = getSettings(Unit).first()

            _initialEntry.value = !settings.onboardingFinished
        }
    }

    /**
     * The current state of the app.
     */
    val state = getSettings(Unit)
        .map { AppState(it) }
        .stateIn(null)

    override suspend fun handleEvent(event: UiEvent) {

    }

}

/**
 * The state of the app.
 */
internal data class AppState(

    /**
     * The current settings.
     */
    val settings: Settings

)