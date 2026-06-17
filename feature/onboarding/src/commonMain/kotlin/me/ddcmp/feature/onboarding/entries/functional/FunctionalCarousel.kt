package me.ddcmp.feature.onboarding.entries.functional

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.onboarding.generated.resources.Res
import dream_diary_cmp.feature.onboarding.generated.resources.confidentiality_description
import dream_diary_cmp.feature.onboarding.generated.resources.confidentiality_title
import dream_diary_cmp.feature.onboarding.generated.resources.lock_continue_label
import dream_diary_cmp.feature.onboarding.generated.resources.lock_description
import dream_diary_cmp.feature.onboarding.generated.resources.lock_title
import dream_diary_cmp.feature.onboarding.generated.resources.lock_unlock_label
import dream_diary_cmp.feature.onboarding.generated.resources.theme_description
import dream_diary_cmp.feature.onboarding.generated.resources.theme_dynamic_description
import dream_diary_cmp.feature.onboarding.generated.resources.theme_dynamic_label
import dream_diary_cmp.feature.onboarding.generated.resources.theme_mode_label
import dream_diary_cmp.feature.onboarding.generated.resources.theme_title
import me.bumiller.ddcmp.domain.model.ColorMode
import me.bumiller.ddcmp.domain.model.ColorTheme
import me.bumiller.ddcmp.domain.model.Confidentiality
import me.bumiller.ddcmp.domain.model.Settings
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.biometric.BiometricContainer
import me.bumiller.ddcmp.ui.common.biometric.BiometricResult
import me.bumiller.ddcmp.ui.common.biometric.biometricAvailable
import me.bumiller.ddcmp.ui.common.biometric.rememberBiometricState
import me.bumiller.ddcmp.ui.common.style.DisabledContent
import me.bumiller.ddcmp.ui.common.style.dynamicColorAvailable
import me.bumiller.ddcmp.ui.common.translation.translateDescription
import me.bumiller.ddcmp.ui.common.translation.translateName
import me.ddcmp.feature.onboarding.OnboardingStyles
import me.ddcmp.feature.onboarding.entries.functional.FunctionalOnboardingViewmodel.UiEvent
import me.ddcmp.feature.onboarding.entries.functional.FunctionalOnboardingViewmodel.UiState
import me.ddcmp.feature.onboarding.entries.functional.FunctionalOnboardingViewmodel.ViewModelEvent
import me.ddcmp.feature.onboarding.layout.BaseOnboardingLayout
import me.ddcmp.feature.onboarding.layout.OnboardingPager
import me.ddcmp.feature.onboarding.layout.OnboardingTextSection
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun FunctionalCarousel(
    onContinue: () -> Unit
) {
    ViewModelScope<UiEvent, ViewModelEvent, FunctionalOnboardingViewmodel>(
        onViewModelEvent = {}
    ) { vm ->
        val settings by vm.settings.collectAsStateWithLifecycle()
        val uiState by vm.uiState.collectAsStateWithLifecycle()

        FunctionalCarousel(vm::onEvent, settings, uiState, onContinue)
    }
}

private enum class FunctionalScreens {
    Confidentiality, Theme, Lock
}

@Composable
private fun FunctionalCarousel(
    onEvent: (UiEvent) -> Unit,
    settings: Settings,
    uiState: UiState,
    onContinue: () -> Unit
) {
    val screens = FunctionalScreens.entries.toList().let {
        if (!biometricAvailable()) it - FunctionalScreens.Lock
        else it
    }
    OnboardingPager(
        modifier = Modifier
            .fillMaxSize(),
        screens = screens
    ) { screen ->
        when (screen) {
            FunctionalScreens.Confidentiality -> ConfidentialityScreen(
                settings = settings,
                onSelectConfidentiality = { onEvent(UiEvent.SelectConfidentiality(it)) }
            )

            FunctionalScreens.Theme -> ThemeScreen(
                onSelectMode = { onEvent(UiEvent.SelectColorMode(it)) },
                onSelectTheme = { onEvent(UiEvent.SelectColorTheme(it)) },
                onChangeDropdownState = { onEvent(UiEvent.ToggleColorModeDropdown(it)) },
                settings = settings,
                uiState = uiState
            )

            FunctionalScreens.Lock -> LockScreen(
                onSetUnlock = { onEvent(UiEvent.SetUnlock(it)) },
                settings = settings,
                onContinue = {
                    onEvent(UiEvent.FinishOnboarding)
                    onContinue()
                }
            )
        }
    }
}

@Composable
private fun LockScreen(
    onSetUnlock: (lock: Boolean) -> Unit,
    settings: Settings,
    onContinue: () -> Unit
) {
    val biometricState = rememberBiometricState()

    BiometricContainer(
        state = biometricState,
        onResult = {
            if (it == BiometricResult.AuthenticationSuccess) onSetUnlock(true)
        }
    )

    BaseOnboardingLayout(
        modifier = Modifier
            .fillMaxSize(),
        primary = {
            OnboardingTextSection(
                title = stringResource(Res.string.lock_title),
                description = stringResource(Res.string.lock_description),
                slimWidth = false
            )
        },
        verticalPrimaryWeight = 0.5F,
        secondary = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(
                    space = 16.dp,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                if (settings.unlock) onSetUnlock(false)
                                else biometricState.requestAuth()
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = settings.unlock,
                        onCheckedChange = {
                            if (settings.unlock) onSetUnlock(false)
                            else biometricState.requestAuth()
                        }
                    )
                    Text(
                        text = stringResource(Res.string.lock_unlock_label),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Button(
                    modifier = Modifier
                        .width(300.dp),
                    onClick = onContinue
                ) {
                    Text(stringResource(Res.string.lock_continue_label))
                }
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeScreen(
    onSelectMode: (ColorMode) -> Unit,
    onSelectTheme: (ColorTheme) -> Unit,
    onChangeDropdownState: (Boolean) -> Unit,
    settings: Settings,
    uiState: UiState
) {
    BaseOnboardingLayout(
        modifier = Modifier
            .fillMaxSize(),
        primary = {
            OnboardingTextSection(
                title = stringResource(Res.string.theme_title),
                description = stringResource(Res.string.theme_description),
                slimWidth = false
            )
        },
        primaryPadding = false,
        verticalPrimaryWeight = 0.5F,
        secondary = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = uiState.colorModeDropdownExpanded,
                    onExpandedChange = onChangeDropdownState
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryEditable),
                        value = settings.colorMode.translateName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(Res.string.theme_mode_label)) }
                    )
                    ExposedDropdownMenu(
                        expanded = uiState.colorModeDropdownExpanded,
                        onDismissRequest = { onChangeDropdownState(false) }
                    ) {
                        ColorMode.entries.forEach { mode ->
                            DropdownMenuItem(
                                onClick = {
                                    onSelectMode(mode)
                                    onChangeDropdownState(false)
                                },
                                text = { Text(mode.translateName) }
                            )
                        }
                    }
                }

                if (dynamicColorAvailable()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(Res.string.theme_dynamic_description),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Row(
                            modifier = Modifier
                                .clickable(
                                    onClick = {
                                        val newTheme =
                                            if (settings.colorTheme == ColorTheme.System) ColorTheme.App
                                            else ColorTheme.System
                                        onSelectTheme(newTheme)
                                    },
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = settings.colorTheme == ColorTheme.System,
                                onCheckedChange = { onSelectTheme(if (it) ColorTheme.System else ColorTheme.App) }
                            )
                            Text(
                                text = stringResource(Res.string.theme_dynamic_label),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                }
            }
        }
    )
}

@Composable
private fun ConfidentialityScreen(
    onSelectConfidentiality: (Confidentiality) -> Unit,
    settings: Settings
) {
    BaseOnboardingLayout(
        modifier = Modifier
            .fillMaxSize(),
        primary = {
            OnboardingTextSection(
                title = stringResource(Res.string.confidentiality_title),
                description = stringResource(Res.string.confidentiality_description),
                slimWidth = false
            )
        },
        primaryPadding = false,
        verticalPrimaryWeight = 0.5F,
        secondary = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Confidentiality.entries.forEach {
                    ConfidentialityItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                        confidentiality = it,
                        selected = it == settings.defaultConfidentiality,
                        onSelect = { onSelectConfidentiality(it) }
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ConfidentialityItem(
    modifier: Modifier,
    confidentiality: Confidentiality,
    onSelect: () -> Unit,
    selected: Boolean
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onSelect),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        DisabledContent(!selected) {
            Column {
                Text(
                    text = confidentiality.translateName,
                    style = OnboardingStyles.confidentialityItemTitle
                )
                Text(
                    text = confidentiality.translateDescription,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}