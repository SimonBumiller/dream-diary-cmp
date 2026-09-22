package me.ddcmp.feature.import.entries.configuration

import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import me.bumiller.ddcmp.domain.model.IOFileType
import me.bumiller.ddcmp.domain.model.ImportOption
import me.bumiller.ddcmp.domain.usecase.io.ImportUseCase
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.ddcmp.feature.import.entries.configuration.ConfigurationViewModel.UiEvent
import me.ddcmp.feature.import.entries.configuration.ConfigurationViewModel.ViewModelEvent
import me.ddcmp.feature.import.model.ImportOptionType

internal class ConfigurationViewModel(
    private val import: ImportUseCase
): AppViewModel<UiEvent, ViewModelEvent>() {

    init {
        registerUiState(UiState())
    }

    /**
     * The ui state flow.
     */
    val uiState = uiState<UiState>().asStateFlow()

    val state = uiState.map(::createState)
        .stateIn(createState(UiState()))

    data class UiState(
        val type: ImportOptionType = ImportOptionType.Normal,
        val transferFavorites: Boolean = true,
        val timeZone: TimeZone = TimeZone.currentSystemDefault(),
        val fileType: IOFileType = IOFileType.Json,
        val showTimeZoneDropdown: Boolean = false,
        val showFileTypeDropdown: Boolean = false,
        val file: PlatformFile? = null,
        val importing: Boolean = false
    )

    data class State(
        val option: ImportOption
    )

    private fun createState(uiState: UiState) = State(
        option = when (uiState.type) {
            ImportOptionType.Legacy -> ImportOption.Legacy(
                timeZone = uiState.timeZone,
                transferFavorites = uiState.transferFavorites
            )
            ImportOptionType.Normal -> ImportOption.Normal(
                fileType = uiState.fileType,
                transferFavorites = uiState.transferFavorites
            )
        }
    )

    override suspend fun handleEvent(event: UiEvent) = when (event) {
        is UiEvent.ChangeFileType -> updateUiState<UiState> {
            it.copy(fileType = event.fileType)
        }
        is UiEvent.ChangeTimeZone -> updateUiState<UiState> {
            it.copy(timeZone = event.timeZone)
        }
        is UiEvent.ChangeTransferFavorites -> updateUiState<UiState> {
            it.copy(transferFavorites = event.transferFavorites)
        }
        is UiEvent.ChangeType -> updateUiState<UiState> {
            it.copy(type = event.type)
        }
        is UiEvent.Return -> fireEvent(ViewModelEvent.GoBack)
        is UiEvent.ToggleTimeZoneDropdownVisibility -> updateUiState<UiState> {
            it.copy(showTimeZoneDropdown = event.show)
        }
        is UiEvent.ToggleFileTypeDropdownVisibility -> updateUiState<UiState> {
            it.copy(showFileTypeDropdown = event.show)
        }
        is UiEvent.FileSelected -> {
            updateUiState<UiState> {
                it.copy(file = event.file)
            }
        }
        UiEvent.StartImport -> {
            val file = uiState.value.file ?: return
            val options = state.value.option

            viewModelScope.launch {
                updateUiState<UiState> {
                    it.copy(importing = true)
                }

                val resultId = import(ImportUseCase.I(file, options))

                fireEvent(ViewModelEvent.Finished(resultId))

                updateUiState<UiState> {
                    it.copy(importing = false)
                }
            }

            Unit
        }
    }

    sealed interface UiEvent: UIE {
        data class ChangeType(val type: ImportOptionType): UiEvent
        data class ChangeTransferFavorites(val transferFavorites: Boolean): UiEvent
        data class ChangeTimeZone(val timeZone: TimeZone): UiEvent
        data class ChangeFileType(val fileType: IOFileType): UiEvent
        data class ToggleTimeZoneDropdownVisibility(val show: Boolean): UiEvent
        data class ToggleFileTypeDropdownVisibility(val show: Boolean): UiEvent
        data object Return: UiEvent
        data object StartImport: UiEvent
        data class FileSelected(val file: PlatformFile?): UiEvent
    }

    sealed interface ViewModelEvent: VME {
        data class Finished(val resultId: Long): ViewModelEvent
        data object GoBack: ViewModelEvent
    }

}