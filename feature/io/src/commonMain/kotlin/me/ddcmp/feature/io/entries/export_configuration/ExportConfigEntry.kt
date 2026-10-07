package me.ddcmp.feature.io.entries.export_configuration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.io.generated.resources.Res
import dream_diary_cmp.feature.io.generated.resources.export_configuration_description
import dream_diary_cmp.feature.io.generated.resources.export_configuration_file_description
import dream_diary_cmp.feature.io.generated.resources.export_configuration_file_name_placeholder
import dream_diary_cmp.feature.io.generated.resources.export_configuration_filetype_description
import dream_diary_cmp.feature.io.generated.resources.export_configuration_filetype_label
import dream_diary_cmp.feature.io.generated.resources.export_configuration_title
import dream_diary_cmp.feature.io.generated.resources.export_suggested_filename
import io.github.vinceglb.filekit.dialogs.compose.SaverResultLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.name
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.IOFileType
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.component.BackIconButton
import me.bumiller.ddcmp.ui.common.layout.TopBarWithProgressIndicator
import me.bumiller.ddcmp.ui.common.plus
import me.bumiller.ddcmp.ui.common.translation.translateName
import me.ddcmp.feature.io.components.IOOptionTypeSelection
import me.ddcmp.feature.io.entries.export_configuration.ExportConfigViewModel.UiEvent
import me.ddcmp.feature.io.entries.export_configuration.ExportConfigViewModel.UiState
import me.ddcmp.feature.io.entries.export_configuration.ExportConfigViewModel.ViewModelEvent
import me.ddcmp.feature.io.model.IOOptionType
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock

@Composable
internal fun ExportConfigEntry(
    onExport: (resultId: Long) -> Unit,
    onReturn: () -> Unit
) {
    ViewModelScope<UiEvent, ViewModelEvent, ExportConfigViewModel>(
        onViewModelEvent = {
            when (it) {
                ViewModelEvent.GoBack -> onReturn()
                is ViewModelEvent.Finished -> onExport(it.resultId)
            }
        }
    ) {
        val uiState by it.uiState.collectAsStateWithLifecycle()

        ExportConfigEntry(it::onEvent, uiState)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ExportConfigEntry(
    onEvent: (UiEvent) -> Unit,
    uiState: UiState
) {
    val picker = rememberFileSaverLauncher {
        onEvent(UiEvent.FileSelected(it))
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBarWithProgressIndicator(
                loading = uiState.exporting
            ) {
                CenterAlignedTopAppBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = {
                        Text(
                            text = stringResource(Res.string.export_configuration_title)
                        )
                    },
                    actions = {
                        FilledIconButton(
                            onClick = { onEvent(UiEvent.StartExport) },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .size(IconButtonDefaults.extraSmallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
                            enabled = !uiState.exporting && uiState.file != null
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(IconButtonDefaults.extraSmallIconSize)
                            )
                        }
                    },
                    navigationIcon = {
                        BackIconButton { onEvent(UiEvent.Return) }
                    }
                )
            }
        }
    ) { pv ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv + PaddingValues(horizontal = 16.dp)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.export_configuration_description),
                style = MaterialTheme.typography.bodySmall
            )

            IOOptionTypeSelection(
                modifier = Modifier
                    .fillMaxWidth(),
                type = uiState.type,
                onTypeChange = { onEvent(UiEvent.ChangeType(it)) }
            )

            Text(
                text = stringResource(Res.string.export_configuration_file_description),
                style = MaterialTheme.typography.bodySmall
            )

            FilePickerSection(picker, uiState)

            if(uiState.type == IOOptionType.Normal) {
                HorizontalDivider()
            }

            ExportOptions(uiState, onEvent)
        }
    }
}

@Composable
private fun ExportOptions(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit
) {
    if(uiState.type == IOOptionType.Normal) {
        FileTypeOption(uiState, onEvent)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FileTypeOption(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit
) {
    Column {
        Text(
            text = stringResource(Res.string.export_configuration_filetype_description),
            style = MaterialTheme.typography.bodySmall
        )

        ExposedDropdownMenuBox(
            expanded = uiState.showFileTypeDropdown,
            onExpandedChange = { onEvent(UiEvent.ToggleFileTypeDropdownVisibility(!uiState.showFileTypeDropdown)) }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
                value = uiState.fileType.translateName,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(Res.string.export_configuration_filetype_label)) }
            )
            ExposedDropdownMenu(
                expanded = uiState.showFileTypeDropdown,
                onDismissRequest = {
                    onEvent(
                        UiEvent.ToggleFileTypeDropdownVisibility(
                            false
                        )
                    )
                }
            ) {
                IOFileType.entries.forEach { filetype ->
                    DropdownMenuItem(
                        onClick = {
                            onEvent(UiEvent.ChangeFileType(filetype))
                            onEvent(UiEvent.ToggleFileTypeDropdownVisibility(false))
                        },
                        text = { Text(filetype.translateName) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilePickerSection(
    picker: SaverResultLauncher,
    uiState: UiState
) {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
    val fileNameSuggestion = stringResource(Res.string.export_suggested_filename, today)

    // The ExposedDropdownMenuBox is needed so we can capture proper clicks on the TextField
    ExposedDropdownMenuBox(
        expanded = false,
        onExpandedChange = {
            picker.launch(
                suggestedName = fileNameSuggestion,
                extension = "zip"
            )
        }
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(
                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                enabled = true
            ),
            value = uiState.file?.name ?: "",
            readOnly = true,
            onValueChange = { },
            placeholder = {
                Text(stringResource(Res.string.export_configuration_file_name_placeholder))
            }
        )
    }
}