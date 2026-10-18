package me.ddcmp.feature.io.entries.export_configuration

import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.bumiller.ddcmp.domain.model.io.ExportOption
import me.bumiller.ddcmp.domain.model.io.IOFileType
import me.bumiller.ddcmp.domain.usecase.io.ExportUseCase
import me.bumiller.ddcmp.domain.util.AppCoroutineScope
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.ddcmp.feature.io.entries.export_configuration.ExportConfigViewModel.UiEvent
import me.ddcmp.feature.io.entries.export_configuration.ExportConfigViewModel.ViewModelEvent
import me.bumiller.ddcmp.ui.common.model.IOOptionType

internal class ExportConfigViewModel(
    private val export: ExportUseCase,
    private val appScope: AppCoroutineScope
) : AppViewModel<UiEvent, ViewModelEvent>() {

    init {
        registerUiState(UiState())
    }

    /**
     * The ui state flow.
     */
    val uiState = uiState<UiState>().asStateFlow()

    data class UiState(
        val type: IOOptionType = IOOptionType.Normal,
        val fileType: IOFileType = IOFileType.Json,
        val showFileTypeDropdown: Boolean = false,
        val file: PlatformFile? = null,
        val exporting: Boolean = false
    )

    private fun createExportOptions(uiState: UiState) = when (uiState.type) {
        IOOptionType.Legacy -> ExportOption.Legacy
        IOOptionType.Normal -> ExportOption.Normal(uiState.fileType)
    }

    override suspend fun handleEvent(event: UiEvent) = when (event) {
        is UiEvent.ChangeFileType -> updateUiState<UiState> {
            it.copy(fileType = event.fileType)
        }

        is UiEvent.ChangeType -> updateUiState<UiState> {
            it.copy(type = event.type)
        }

        is UiEvent.Return -> fireEvent(ViewModelEvent.GoBack)
        is UiEvent.ToggleFileTypeDropdownVisibility -> updateUiState<UiState> {
            it.copy(showFileTypeDropdown = event.show)
        }

        is UiEvent.FileSelected -> {
            updateUiState<UiState> {
                it.copy(file = event.file)
            }
        }

        UiEvent.StartExport -> {
            val file = uiState.value.file ?: return
            val options = createExportOptions(uiState.value)

            appScope.launch {
                updateUiState<UiState> {
                    it.copy(exporting = true)
                }

                val resultId = export(ExportUseCase.I(file, options))

                fireEvent(ViewModelEvent.Finished(resultId))
                updateUiState<UiState> {
                    it.copy(exporting = false)
                }
            }

            Unit
        }
    }

    sealed interface UiEvent : UIE {
        data class ChangeType(val type: IOOptionType) : UiEvent
        data class ChangeFileType(val fileType: IOFileType) : UiEvent
        data class ToggleFileTypeDropdownVisibility(val show: Boolean) : UiEvent
        data object Return : UiEvent
        data object StartExport : UiEvent
        data class FileSelected(val file: PlatformFile?) : UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data class Finished(val resultId: Long) : ViewModelEvent
        data object GoBack : ViewModelEvent
    }

}