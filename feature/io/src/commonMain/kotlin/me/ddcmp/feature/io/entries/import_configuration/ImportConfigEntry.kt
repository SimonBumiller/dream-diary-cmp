package me.ddcmp.feature.io.entries.import_configuration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.io.generated.resources.Res
import dream_diary_cmp.feature.io.generated.resources.import_configuration_description
import dream_diary_cmp.feature.io.generated.resources.import_configuration_file_description
import dream_diary_cmp.feature.io.generated.resources.import_configuration_file_name_placeholder
import dream_diary_cmp.feature.io.generated.resources.import_configuration_options_description
import dream_diary_cmp.feature.io.generated.resources.import_configuration_timezone_label
import dream_diary_cmp.feature.io.generated.resources.import_configuration_title
import dream_diary_cmp.feature.io.generated.resources.import_configuration_transfer_favorites_label
import kotlinx.datetime.TimeZone
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.component.BackIconButton
import me.bumiller.ddcmp.ui.common.component.FileSelectorTextField
import me.bumiller.ddcmp.ui.common.component.IOFileSelector
import me.bumiller.ddcmp.ui.common.component.IOOptionTypeSelection
import me.bumiller.ddcmp.ui.common.layout.TopBarWithProgressIndicator
import me.bumiller.ddcmp.ui.common.model.IOOptionType
import me.bumiller.ddcmp.ui.common.plus
import me.ddcmp.feature.io.entries.import_configuration.ImportConfigViewModel.State
import me.ddcmp.feature.io.entries.import_configuration.ImportConfigViewModel.UiEvent
import me.ddcmp.feature.io.entries.import_configuration.ImportConfigViewModel.UiState
import me.ddcmp.feature.io.entries.import_configuration.ImportConfigViewModel.ViewModelEvent
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ImportConfigEntry(
    onImport: (resultId: Long) -> Unit,
    onReturn: () -> Unit
) {
    ViewModelScope<UiEvent, ViewModelEvent, ImportConfigViewModel>(
        onViewModelEvent = {
            when (it) {
                ViewModelEvent.GoBack -> onReturn()
                is ViewModelEvent.Finished -> onImport(it.resultId)
            }
        }
    ) {
        val uiState by it.uiState.collectAsStateWithLifecycle()
        val state by it.state.collectAsStateWithLifecycle()

        ImportConfigEntry(it::onEvent, uiState, state)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ImportConfigEntry(
    onEvent: (UiEvent) -> Unit,
    uiState: UiState,
    state: State
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBarWithProgressIndicator(
                loading = uiState.importing
            ) {
                CenterAlignedTopAppBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = {
                        Text(
                            text = stringResource(Res.string.import_configuration_title)
                        )
                    },
                    actions = {
                        FilledIconButton(
                            onClick = { onEvent(UiEvent.StartImport) },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .size(IconButtonDefaults.extraSmallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
                            enabled = !uiState.importing && uiState.file != null
                        ) {
                            Icon(
                                imageVector = Icons.Default.Publish,
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
                text = stringResource(Res.string.import_configuration_description),
                style = MaterialTheme.typography.bodySmall
            )

            IOOptionTypeSelection(
                modifier = Modifier
                    .fillMaxWidth(),
                type = uiState.type,
                onTypeChange = { onEvent(UiEvent.ChangeType(it)) }
            )

            Text(
                text = stringResource(Res.string.import_configuration_file_description),
                style = MaterialTheme.typography.bodySmall
            )

            FilePickerSection(uiState, state)

            HorizontalDivider()

            ImportOptions(uiState, onEvent)
        }
    }
}

@Composable
private fun ImportOptions(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit
) {
    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(Res.string.import_configuration_options_description),
            style = MaterialTheme.typography.bodySmall
        )

        KeepFavoriteOption(uiState, onEvent)

        if (uiState.type == IOOptionType.Legacy) {
            TimeZoneOption(uiState, onEvent)
        }

        if (uiState.type == IOOptionType.Normal) {
            FileTypeOption(uiState, onEvent)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FileTypeOption(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit
) {
    IOFileSelector(
        showDropdown = uiState.showFileTypeDropdown,
        toggleDropdownVisibility = { onEvent(UiEvent.ToggleFileTypeDropdownVisibility(!uiState.showFileTypeDropdown)) },
        onTypeSelected = { onEvent(UiEvent.ChangeFileType(it)) },
        fileType = uiState.fileType
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TimeZoneOption(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = uiState.showTimeZoneDropdown,
        onExpandedChange = { onEvent(UiEvent.ToggleTimeZoneDropdownVisibility(!uiState.showTimeZoneDropdown)) }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
            value = uiState.timeZone.id,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(Res.string.import_configuration_timezone_label)) }
        )
        ExposedDropdownMenu(
            expanded = uiState.showTimeZoneDropdown,
            onDismissRequest = {
                onEvent(
                    UiEvent.ToggleTimeZoneDropdownVisibility(
                        false
                    )
                )
            }
        ) {
            TimeZone.availableZoneIds.forEach { id ->
                DropdownMenuItem(
                    onClick = {
                        onEvent(UiEvent.ChangeTimeZone(TimeZone.of(id)))
                        onEvent(UiEvent.ToggleTimeZoneDropdownVisibility(false))
                    },
                    text = { Text(id) }
                )
            }
        }
    }
}

@Composable
private fun KeepFavoriteOption(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = uiState.transferFavorites,
            onCheckedChange = { onEvent(UiEvent.ChangeTransferFavorites(it)) }
        )
        Text(
            text = stringResource(Res.string.import_configuration_transfer_favorites_label),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilePickerSection(
    uiState: UiState,
    state: State
) {

    FileSelectorTextField(
        modifier = Modifier,
        file = uiState.file,
        onFileChanged = { UiEvent.FileSelected(it) },
        extension = state.option.backupFileExtension,
        placeholder = stringResource(Res.string.import_configuration_file_name_placeholder)
    )
}