package me.ddcmp.feature.io.entries.export_result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.bumiller.ddcmp.domain.model.ExportResult
import me.bumiller.ddcmp.domain.usecase.io.GetExportLogUsecase
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.ddcmp.feature.io.entries.export_result.ExportResultViewModel.UiEvent
import me.ddcmp.feature.io.entries.export_result.ExportResultViewModel.ViewModelEvent
import me.ddcmp.feature.io.navigation.IORoutes

internal class ExportResultViewModel(
    savedStateHandle: SavedStateHandle,
    getExportLog: GetExportLogUsecase
) : AppViewModel<UiEvent, ViewModelEvent>() {

    val result = MutableStateFlow<ExportResult?>(null)

    init {
        val route = savedStateHandle.toRoute<IORoutes.ExportResult>()
        val id = route.resultId

        viewModelScope.launch {
            val result = getExportLog(id)
            this@ExportResultViewModel.result.emit(result)
        }
    }

    override suspend fun handleEvent(event: UiEvent) = when (event) {
        is UiEvent.Continue -> fireEvent(ViewModelEvent.Continue(event.wasSuccess))
    }

    sealed interface UiEvent : UIE {
        data class Continue(val wasSuccess: Boolean) : UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data class Continue(val wasSuccess: Boolean) : ViewModelEvent
    }

}