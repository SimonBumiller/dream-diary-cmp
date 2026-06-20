package me.ddcmp.feature.import.entries.configuration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.import.generated.resources.Res
import dream_diary_cmp.feature.import.generated.resources.configuration_actions_back
import dream_diary_cmp.feature.import.generated.resources.configuration_configuration_header
import dream_diary_cmp.feature.import.generated.resources.configuration_description
import dream_diary_cmp.feature.import.generated.resources.configuration_file_name_placeholder
import dream_diary_cmp.feature.import.generated.resources.configuration_filetype_label
import dream_diary_cmp.feature.import.generated.resources.configuration_pick_file_header
import dream_diary_cmp.feature.import.generated.resources.configuration_pick_file_label
import dream_diary_cmp.feature.import.generated.resources.configuration_timezone_label
import dream_diary_cmp.feature.import.generated.resources.configuration_title
import dream_diary_cmp.feature.import.generated.resources.configuration_transfer_favorites_label
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.name
import kotlinx.datetime.TimeZone
import me.bumiller.ddcmp.domain.model.IOFileType
import me.bumiller.ddcmp.domain.model.ImportResult
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.component.RadioSelectors
import me.bumiller.ddcmp.ui.common.layout.TopBarWithProgressIndicator
import me.bumiller.ddcmp.ui.common.plus
import me.bumiller.ddcmp.ui.common.translation.translateName
import me.ddcmp.feature.import.entries.configuration.ConfigurationViewModel.State
import me.ddcmp.feature.import.entries.configuration.ConfigurationViewModel.UiEvent
import me.ddcmp.feature.import.entries.configuration.ConfigurationViewModel.UiState
import me.ddcmp.feature.import.entries.configuration.ConfigurationViewModel.ViewModelEvent
import me.ddcmp.feature.import.model.ImportOptionType
import me.ddcmp.feature.import.translation.translateDescription
import me.ddcmp.feature.import.translation.translateName
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ConfigurationEntry(
    onImport: (resultId: Long) -> Unit,
    onReturn: () -> Unit
) {
    ViewModelScope<UiEvent, ViewModelEvent, ConfigurationViewModel>(
        onViewModelEvent = {
            when (it) {
                ViewModelEvent.GoBack -> onReturn()
                is ViewModelEvent.Finished -> onImport(it.resultId)
            }
        }
    ) {
        val uiState by it.uiState.collectAsStateWithLifecycle()
        val state by it.state.collectAsStateWithLifecycle()

        ConfigurationEntry(it::onEvent, uiState, state)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ConfigurationEntry(
    onEvent: (UiEvent) -> Unit,
    uiState: UiState,
    state: State
) {
    val picker = rememberFilePickerLauncher(
        type = FileKitType.File(state.option.fileExtensions)
    ) {
        onEvent(UiEvent.FileSelected(it))
    }

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
                            text = stringResource(Res.string.configuration_title)
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
                        IconButton(
                            onClick = { onEvent(UiEvent.Return) }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = stringResource(Res.string.configuration_actions_back)
                            )
                        }
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
                text = stringResource(Res.string.configuration_description),
                style = MaterialTheme.typography.bodySmall
            )

            RadioSelectors(
                items = listOf(ImportOptionType.Normal, ImportOptionType.Legacy),
                selected = uiState.type,
                onSelect = { onEvent(UiEvent.ChangeType(it)) },
                title = { it.translateName },
                description = { it.translateDescription }
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(Res.string.configuration_configuration_header),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = uiState.transferFavorites,
                        onCheckedChange = { onEvent(UiEvent.ChangeTransferFavorites(it)) }
                    )
                    Text(
                        text = stringResource(Res.string.configuration_transfer_favorites_label),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                if (uiState.type == ImportOptionType.Legacy) {
                    ExposedDropdownMenuBox(
                        expanded = uiState.showTimeZoneDropdown,
                        onExpandedChange = { onEvent(UiEvent.ToggleTimeZoneDropdownVisibility(!uiState.showTimeZoneDropdown)) }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryEditable),
                            value = uiState.timeZone.id,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(Res.string.configuration_timezone_label)) }
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

                if (uiState.type == ImportOptionType.Normal) {
                    ExposedDropdownMenuBox(
                        expanded = uiState.showTimeZoneDropdown,
                        onExpandedChange = { onEvent(UiEvent.ToggleFileTypeDropdownVisibility(!uiState.showFileTypeDropdown)) }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryEditable),
                            value = uiState.fileType.translateName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(Res.string.configuration_filetype_label)) }
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(Res.string.configuration_pick_file_header),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = { picker.launch() }
                    ) {
                        Text(stringResource(Res.string.configuration_pick_file_label))
                    }

                    Text(
                        text = uiState.file?.name
                            ?: stringResource(Res.string.configuration_file_name_placeholder),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}