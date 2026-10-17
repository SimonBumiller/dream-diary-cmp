package me.ddcmp.feature.settings.entries.root

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.settings.generated.resources.Res
import dream_diary_cmp.feature.settings.generated.resources.settings_root_backup_description
import dream_diary_cmp.feature.settings.generated.resources.settings_root_backup_title
import dream_diary_cmp.feature.settings.generated.resources.settings_root_confidentiality_label
import dream_diary_cmp.feature.settings.generated.resources.settings_root_confidentiality_medium_description
import dream_diary_cmp.feature.settings.generated.resources.settings_root_confidentiality_none_description
import dream_diary_cmp.feature.settings.generated.resources.settings_root_confidentiality_section_label
import dream_diary_cmp.feature.settings.generated.resources.settings_root_confidentiality_severe_description
import dream_diary_cmp.feature.settings.generated.resources.settings_root_design_color_mode_title
import dream_diary_cmp.feature.settings.generated.resources.settings_root_design_color_theme_description
import dream_diary_cmp.feature.settings.generated.resources.settings_root_design_color_theme_title
import dream_diary_cmp.feature.settings.generated.resources.settings_root_design_section_label
import dream_diary_cmp.feature.settings.generated.resources.settings_root_export_description
import dream_diary_cmp.feature.settings.generated.resources.settings_root_export_title
import dream_diary_cmp.feature.settings.generated.resources.settings_root_import_description
import dream_diary_cmp.feature.settings.generated.resources.settings_root_import_title
import dream_diary_cmp.feature.settings.generated.resources.settings_root_io_section_label
import dream_diary_cmp.feature.settings.generated.resources.settings_root_title
import dream_diary_cmp.feature.settings.generated.resources.settings_root_unlock_description
import dream_diary_cmp.feature.settings.generated.resources.settings_root_unlock_label
import me.bumiller.ddcmp.domain.model.PlatformFeatures
import me.bumiller.ddcmp.domain.model.dream.Confidentiality
import me.bumiller.ddcmp.domain.model.settings.ColorMode
import me.bumiller.ddcmp.domain.model.settings.ColorTheme
import me.bumiller.ddcmp.domain.model.settings.Settings
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.biometric.BiometricAuthState
import me.bumiller.ddcmp.ui.common.biometric.BiometricContainer
import me.bumiller.ddcmp.ui.common.biometric.BiometricResult
import me.bumiller.ddcmp.ui.common.biometric.rememberBiometricState
import me.bumiller.ddcmp.ui.common.component.BackIconButton
import me.bumiller.ddcmp.ui.common.plus
import me.bumiller.ddcmp.ui.common.style.DisabledContent
import me.bumiller.ddcmp.ui.common.translation.translateName
import me.ddcmp.feature.settings.entries.root.SettingsRootViewmodel.UiEvent
import me.ddcmp.feature.settings.entries.root.SettingsRootViewmodel.UiEvent.ClickBackButton
import me.ddcmp.feature.settings.entries.root.SettingsRootViewmodel.UiState
import me.ddcmp.feature.settings.entries.root.SettingsRootViewmodel.ViewModelEvent
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SettingsRootEntry(
    onBack: () -> Unit,
    onGoToExport: () -> Unit,
    onGoToImport: () -> Unit,
    onGoToOrderConfig: () -> Unit,
    onGoToOrder: (Long) -> Unit
) {
    val biometricState = rememberBiometricState()

    ViewModelScope<UiEvent, ViewModelEvent, SettingsRootViewmodel>(
        onViewModelEvent = {
            when (it) {
                ViewModelEvent.NavigateBack -> onBack()
                ViewModelEvent.NavigateToExport -> onGoToExport()
                ViewModelEvent.NavigateToImport -> onGoToImport()
                ViewModelEvent.NavigateToOrderConfig -> onGoToOrderConfig()
                is ViewModelEvent.NavigateToOrderDetail -> onGoToOrder(it.id)
            }
        }
    ) { vm ->
        val uiState by vm.uiState.collectAsStateWithLifecycle()
        val settings by vm.settings.collectAsStateWithLifecycle()

        BiometricContainer(
            state = biometricState,
            onResult = {
                when (it) {
                    BiometricResult.AuthenticationSuccess -> vm.onEvent(UiEvent.ChangeAppUnlock(true))
                    else -> {}
                }
            }
        )

        if (settings != null) {
            SettingsRootEntry(settings!!, uiState, biometricState, vm::onEvent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsRootEntry(
    settings: Settings,
    uiState: UiState,
    biometricState: BiometricAuthState,
    onEvent: (UiEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(Res.string.settings_root_title))
                },
                navigationIcon = { BackIconButton { onEvent(ClickBackButton) } }
            )
        }
    ) { pv ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv + PaddingValues(horizontal = 8.dp))
        ) {
            DisabledContent {
                Text(
                    text = stringResource(Res.string.settings_root_design_section_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            DesignSection(settings, uiState, onEvent)

            DisabledContent {
                Text(
                    text = stringResource(Res.string.settings_root_confidentiality_section_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            ConfidentialitySection(settings, uiState, biometricState, onEvent)

            DisabledContent {
                Text(
                    text = stringResource(Res.string.settings_root_io_section_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            IoSection(onEvent)
        }
    }
}

@Composable
private fun IoSection(
    onEvent: (UiEvent) -> Unit
) {
    Column {
        ListItem(
            modifier = Modifier.clickable {
                onEvent(UiEvent.ClickExport)
            },
            headlineContent = {
                Text(
                    text = stringResource(Res.string.settings_root_export_title),
                    fontWeight = FontWeight.Bold
                )
            },
            supportingContent = {
                Text(stringResource(Res.string.settings_root_export_description))
            },
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.Download,
                    contentDescription = null
                )
            }
        )
        ListItem(
            modifier = Modifier.clickable {
                onEvent(UiEvent.ClickImport)
            },
            headlineContent = {
                Text(
                    text = stringResource(Res.string.settings_root_import_title),
                    fontWeight = FontWeight.Bold
                )
            },
            supportingContent = {
                Text(stringResource(Res.string.settings_root_import_description))
            },
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.Upload,
                    contentDescription = null
                )
            }
        )

        if(PlatformFeatures.supportsPeriodicBackups) {
            ListItem(
                modifier = Modifier.clickable {
                    onEvent(UiEvent.ClickBackup)
                },
                headlineContent = {
                    Text(
                        text = stringResource(Res.string.settings_root_backup_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                supportingContent = {
                    Text(stringResource(Res.string.settings_root_backup_description))
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@Composable
private fun ConfidentialitySection(
    settings: Settings,
    uiState: UiState,
    biometricState: BiometricAuthState,
    onEvent: (UiEvent) -> Unit
) {
    Column {

        Box {
            ListItem(
                modifier = Modifier.clickable {
                    onEvent(UiEvent.ClickDefaultConfidentiality)
                },
                headlineContent = {
                    Text(
                        text = stringResource(Res.string.settings_root_confidentiality_label),
                        fontWeight = FontWeight.Bold
                    )
                },
                supportingContent = {
                    Text(settings.defaultConfidentiality.translateName)
                }
            )

            DropdownMenu(
                expanded = uiState.showDefaultConfidentialityDropdown,
                onDismissRequest = { onEvent(UiEvent.ClickDefaultConfidentiality) }
            ) {
                Confidentiality.entries.forEach { confidentiality ->
                    val title = confidentiality.translateName
                    val description = when (confidentiality) {
                        Confidentiality.None -> Res.string.settings_root_confidentiality_none_description
                        Confidentiality.Medium -> Res.string.settings_root_confidentiality_medium_description
                        Confidentiality.Severe -> Res.string.settings_root_confidentiality_severe_description
                    }.let { stringResource(it) }

                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(title)
                                Text(
                                    text = description,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        },
                        onClick = { onEvent(UiEvent.ChangeDefaultConfidentiality(confidentiality)) }
                    )
                }

            }
        }

        HorizontalDivider()

        if (PlatformFeatures.supportsBiometricAuth) {
            ListItem(
                modifier = Modifier.clickable {
                    if (settings.unlock)
                        onEvent(UiEvent.ChangeAppUnlock(false))
                    else biometricState.requestAuth()
                },
                headlineContent = {
                    Text(
                        text = stringResource(Res.string.settings_root_unlock_label),
                        fontWeight = FontWeight.Bold
                    )
                },
                supportingContent = {
                    Text(
                        text = stringResource(Res.string.settings_root_unlock_description),
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                trailingContent = {
                    Switch(
                        checked = settings.unlock,
                        onCheckedChange = {
                            if (settings.unlock)
                                onEvent(UiEvent.ChangeAppUnlock(false))
                            else biometricState.requestAuth()
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun DesignSection(
    settings: Settings,
    uiState: UiState,
    onEvent: (UiEvent) -> Unit
) {
    Column {
        Box {
            ListItem(
                modifier = Modifier.clickable {
                    onEvent(UiEvent.ClickColorMode)
                },
                headlineContent = {
                    Text(
                        text = stringResource(Res.string.settings_root_design_color_mode_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                supportingContent = {
                    Text(settings.colorMode.translateName)
                }
            )

            DropdownMenu(
                expanded = uiState.showColorModeDropdown,
                onDismissRequest = { onEvent(UiEvent.ClickColorMode) }
            ) {
                ColorMode.entries.forEach { colorMode ->
                    DropdownMenuItem(
                        text = { Text(colorMode.translateName) },
                        onClick = { onEvent(UiEvent.ChangeColorMode(colorMode)) }
                    )
                }

            }
        }

        HorizontalDivider()

        ListItem(
            modifier = Modifier.clickable {
                val theme = when (settings.colorTheme) {
                    ColorTheme.App -> ColorTheme.System
                    ColorTheme.System -> ColorTheme.App
                }
                onEvent(UiEvent.ChangeColorTheme(theme))
            },
            headlineContent = {
                Text(
                    text = stringResource(Res.string.settings_root_design_color_theme_title),
                    fontWeight = FontWeight.Bold
                )
            },
            supportingContent = {
                Text(
                    text = stringResource(Res.string.settings_root_design_color_theme_description),
                    style = MaterialTheme.typography.bodySmall
                )
            },
            trailingContent = {
                Switch(
                    checked = settings.colorTheme == ColorTheme.System,
                    onCheckedChange = {
                        val theme = when (it) {
                            true -> ColorTheme.System
                            false -> ColorTheme.App
                        }
                        onEvent(UiEvent.ChangeColorTheme(theme))
                    }
                )
            }
        )
    }
}