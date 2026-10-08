package me.ddcmp.feature.settings.entries.root

import kotlinx.coroutines.flow.asStateFlow
import me.bumiller.ddcmp.domain.model.settings.ColorMode
import me.bumiller.ddcmp.domain.model.settings.ColorTheme
import me.bumiller.ddcmp.domain.model.dream.Confidentiality
import me.bumiller.ddcmp.domain.model.settings.Settings
import me.bumiller.ddcmp.domain.usecase.settings.GetSettingsUseCase
import me.bumiller.ddcmp.domain.usecase.settings.SetSettingsUseCase
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.ddcmp.feature.settings.entries.root.SettingsRootViewmodel.UiEvent
import me.ddcmp.feature.settings.entries.root.SettingsRootViewmodel.ViewModelEvent
import me.ddcmp.feature.settings.entries.root.SettingsRootViewmodel.ViewModelEvent.NavigateBack

internal class SettingsRootViewmodel(
    getSettings: GetSettingsUseCase,
    private val setSettings: SetSettingsUseCase
) : AppViewModel<UiEvent, ViewModelEvent>() {

    init {
        registerUiState(UiState())
    }

    /**
     * The settings state.
     */
    val settings = getSettings(Unit)
        .stateIn(null)

    /**
     * The ui state.
     */
    val uiState = uiState<UiState>()
        .asStateFlow()

    private suspend fun updateSettings(updater: Settings.() -> Settings) =
        settings.value?.let(updater)?.let { setSettings(it) } ?: Unit

    override suspend fun handleEvent(event: UiEvent) = when (event) {
        is UiEvent.ChangeAppUnlock -> updateSettings { copy(unlock = !unlock) }
        is UiEvent.ChangeColorMode -> {
            updateSettings { copy(colorMode = event.mode) }
            updateUiState<UiState> { it.copy(showColorModeDropdown = false) }
        }
        is UiEvent.ChangeColorTheme -> updateSettings { copy(colorTheme = event.theme) }
        is UiEvent.ChangeDefaultConfidentiality -> {
            updateSettings { copy(defaultConfidentiality = event.confidentiality) }
            updateUiState<UiState> { it.copy(showDefaultConfidentialityDropdown = false) }
        }
        UiEvent.ClickBackButton -> fireEvent(NavigateBack)
        UiEvent.ClickColorMode -> updateUiState<UiState> { it.copy(showColorModeDropdown = !it.showColorModeDropdown) }
        UiEvent.ClickDefaultConfidentiality -> updateUiState<UiState> { it.copy(showDefaultConfidentialityDropdown = !it.showDefaultConfidentialityDropdown) }
        UiEvent.ClickExport -> fireEvent(ViewModelEvent.NavigateToExport)
        UiEvent.ClickImport -> fireEvent(ViewModelEvent.NavigateToImport)
    }

    data class UiState(
        val showColorModeDropdown: Boolean = false,
        val showDefaultConfidentialityDropdown: Boolean = false
    )

    sealed interface UiEvent : UIE {
        data object ClickBackButton : UiEvent
        data object ClickImport : UiEvent
        data object ClickExport : UiEvent
        data object ClickColorMode : UiEvent
        data object ClickDefaultConfidentiality : UiEvent
        data class ChangeColorMode(val mode: ColorMode) : UiEvent
        data class ChangeColorTheme(val theme: ColorTheme) : UiEvent
        data class ChangeDefaultConfidentiality(val confidentiality: Confidentiality) : UiEvent
        data class ChangeAppUnlock(val unlock: Boolean) : UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data object NavigateBack : ViewModelEvent
        data object NavigateToImport : ViewModelEvent
        data object NavigateToExport : ViewModelEvent
    }

}