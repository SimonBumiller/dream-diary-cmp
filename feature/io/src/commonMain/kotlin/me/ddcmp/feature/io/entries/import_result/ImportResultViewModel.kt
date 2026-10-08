package me.ddcmp.feature.io.entries.import_result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.bumiller.ddcmp.domain.model.io.ImportResult
import me.bumiller.ddcmp.domain.usecase.io.GetImportLogUsecase
import me.bumiller.ddcmp.domain.usecase.settings.GetSettingsUseCase
import me.bumiller.ddcmp.domain.usecase.settings.SetSettingsUseCase
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.ddcmp.feature.io.entries.import_result.ImportResultViewModel.UiEvent
import me.ddcmp.feature.io.entries.import_result.ImportResultViewModel.ViewModelEvent
import me.ddcmp.feature.io.navigation.IORoutes

internal class ImportResultViewModel(
    savedStateHandle: SavedStateHandle,
    getImportLog: GetImportLogUsecase,
    private val setSettings: SetSettingsUseCase,
    private val settings: GetSettingsUseCase,
) : AppViewModel<UiEvent, ViewModelEvent>() {

    val result = MutableStateFlow<ImportResult?>(null)

    init {
        val route = savedStateHandle.toRoute<IORoutes.ImportResult>()
        val id = route.resultId

        viewModelScope.launch {
            val result = getImportLog(id)
            this@ImportResultViewModel.result.emit(result)
        }
    }

    override suspend fun handleEvent(event: UiEvent) = when (event) {
        is UiEvent.Continue -> {
            if(event.wasSuccess) {
                viewModelScope.launch {
                    val settings = settings(Unit).first()
                    setSettings(settings.copy(onboardingFinished = true))
                }
            }

            fireEvent(ViewModelEvent.Continue(event.wasSuccess))
        }
    }

    sealed interface UiEvent : UIE {
        data class Continue(val wasSuccess: Boolean) : UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data class Continue(val wasSuccess: Boolean) : ViewModelEvent
    }

}