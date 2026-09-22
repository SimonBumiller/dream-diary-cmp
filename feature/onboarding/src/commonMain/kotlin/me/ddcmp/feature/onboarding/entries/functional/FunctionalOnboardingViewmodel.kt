package me.ddcmp.feature.onboarding.entries.functional

import kotlinx.coroutines.flow.asStateFlow
import me.bumiller.ddcmp.domain.model.ColorMode
import me.bumiller.ddcmp.domain.model.ColorTheme
import me.bumiller.ddcmp.domain.model.Confidentiality
import me.bumiller.ddcmp.domain.model.Settings
import me.bumiller.ddcmp.domain.usecase.settings.GetSettingsUseCase
import me.bumiller.ddcmp.domain.usecase.settings.SetSettingsUseCase
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME

/**
 * Viewmodel for the functional onboarding carousel.
 */
internal class FunctionalOnboardingViewmodel(

    getSettings: GetSettingsUseCase,

    private val setSettings: SetSettingsUseCase

) : AppViewModel<FunctionalOnboardingViewmodel.UiEvent, FunctionalOnboardingViewmodel.ViewModelEvent>() {

    sealed interface ViewModelEvent : VME {

        /**
         * We go to import.
         */
        data object GoToImport: ViewModelEvent

        data object FinishOnboarding: ViewModelEvent

    }

    sealed interface UiEvent : UIE {

        /**
         * User finished onboarding
         */
        data object FinishOnboarding: UiEvent

        /**
         * User wants to perform import.
         */
        data object DoImport: UiEvent

        /**
         * User selected a new confidentiality settings.
         */
        data class SelectConfidentiality(val confidentiality: Confidentiality) : UiEvent

        /**
         * User toggled the visibility of the color mode dropdown.
         */
        data class ToggleColorModeDropdown(val enabled: Boolean): UiEvent

        /**
         * User selected a color mode.
         */
        data class SelectColorMode(val mode: ColorMode): UiEvent

        /**
         * User selected a color theme.
         */
        data class SelectColorTheme(val theme: ColorTheme): UiEvent

        /**
         * The user changed the unlock setting
         */
        data class SetUnlock(val unlock: Boolean): UiEvent

    }

    /**
     * The mutable ui state.
     */
    data class UiState(

        /**
         * Whether the dropdown for the color mode is expanded.
         */
        val colorModeDropdownExpanded: Boolean = false

    )

    init {
        registerUiState(UiState())
    }

    /**
     * The settings state.
     */
    val settings = getSettings(Unit)
        .stateIn(Settings.Default)

    /**
     * The ui state.
     */
    val uiState = uiState<UiState>().asStateFlow()

    override suspend fun handleEvent(event: UiEvent) = when (event) {
        is UiEvent.SelectConfidentiality -> {
            setSettings(settings.value.copy(defaultConfidentiality = event.confidentiality))
        }

        is UiEvent.SelectColorMode -> {
            setSettings(settings.value.copy(colorMode = event.mode))
        }
        is UiEvent.SelectColorTheme -> {
            setSettings(settings.value.copy(colorTheme = event.theme))
        }
        is UiEvent.ToggleColorModeDropdown -> {
            updateUiState<UiState> {
                it.copy(colorModeDropdownExpanded = event.enabled)
            }
        }

        is UiEvent.SetUnlock -> {
            setSettings(settings.value.copy(unlock = event.unlock))
        }

        is UiEvent.FinishOnboarding -> {
            setSettings(settings.value.copy(onboardingFinished = true))
            fireEvent(ViewModelEvent.FinishOnboarding)
        }

        is UiEvent.DoImport -> fireEvent(ViewModelEvent.GoToImport)
    }
}