package me.ddcmp.feature.home.entries.emotionadd

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import dream_diary_cmp.feature.home.generated.resources.Res
import dream_diary_cmp.feature.home.generated.resources.emotionadd_closest_label
import dream_diary_cmp.feature.home.generated.resources.emotionadd_grid_info
import dream_diary_cmp.feature.home.generated.resources.emotionadd_grid_title
import dream_diary_cmp.feature.home.generated.resources.emotionadd_name_label
import dream_diary_cmp.feature.home.generated.resources.emotionadd_name_placeholder
import dream_diary_cmp.feature.home.generated.resources.emotionadd_title
import kotlinx.coroutines.delay
import me.bumiller.ddcmp.domain.model.Emotion
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.component.BackIconButton
import me.bumiller.ddcmp.ui.common.component.EmotionGrid
import me.bumiller.ddcmp.ui.common.dialog.DiscardChangesDialog
import me.bumiller.ddcmp.ui.common.plus
import me.ddcmp.feature.home.entries.emotionadd.EmotionAddViewModel.UiEvent
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

@Composable
internal fun EmotionAddEntry(
    onBack: () -> Unit
) {
    ViewModelScope<UiEvent, EmotionAddViewModel.ViewModelEvent, EmotionAddViewModel>(
        onViewModelEvent = {
            when (it) {
                EmotionAddViewModel.ViewModelEvent.GoBack -> onBack()
            }
        }
    ) { vm ->
        val uiState by vm.uiState.collectAsStateWithLifecycle()
        val dreamState by vm.emotionState.collectAsStateWithLifecycle()
        val canSave by vm.canSave.collectAsStateWithLifecycle()
        val emotions by vm.emotionsState.collectAsStateWithLifecycle()
        val closest by vm.closest.collectAsStateWithLifecycle()

        DreamAddEntry(vm::onEvent, uiState, dreamState, canSave, emotions, closest)
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalTime::class, ExperimentalComposeUiApi::class
)
@Composable
private fun DreamAddEntry(
    onEvent: (UiEvent) -> Unit,
    uiState: EmotionAddViewModel.UiState,
    emotionState: EmotionAddViewModel.EmotionState,
    canSave: Boolean,
    emotions: List<Emotion>,
    closest: Emotion?
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(300L)
        keyboardController?.run {
            show()
            focusRequester.requestFocus()
        }
    }

    NavigationBackHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None)
    ) {
        onEvent(UiEvent.NavigateBack)
    }

    if (uiState.showDiscardDialog) {
        DiscardChangesDialog(
            onDiscard = { onEvent(UiEvent.NavigateBack) },
            onKeep = { onEvent(UiEvent.HideDiscardDialog) }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(Res.string.emotionadd_title)) },
                actions = {
                    FilledIconButton(
                        onClick = { onEvent(UiEvent.Save) },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .size(IconButtonDefaults.extraSmallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
                        enabled = canSave
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier
                                .size(IconButtonDefaults.extraSmallIconSize)
                        )
                    }
                },
                navigationIcon = {
                    BackIconButton { onEvent(UiEvent.NavigateBack) }
                }
            )
        }
    ) { pv ->
        Column(
            modifier = Modifier
                .padding(pv + PaddingValues(horizontal = 16.dp))
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = emotionState.name,
                onValueChange = { onEvent(UiEvent.ChangeName(it)) },
                placeholder = { Text(stringResource(Res.string.emotionadd_name_placeholder)) },
                label = { Text(stringResource(Res.string.emotionadd_name_label)) },
                keyboardOptions = KeyboardOptions().copy(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrectEnabled = true

                ),
                singleLine = true
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(Res.string.emotionadd_grid_title),
                    style = MaterialTheme.typography.titleMediumEmphasized
                )

                Text(
                    text = stringResource(Res.string.emotionadd_grid_info),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            EmotionGrid(
                modifier = Modifier
                    .fillMaxWidth(),
                existingPoints = emotions.toSet(),
                onClickClosest = { coordinate, _ ->
                    onEvent(UiEvent.ChangeCoordinate(coordinate))
                },
                selectedPoint = emotionState.coordinate
            )

            closest?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.emotionadd_closest_label),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = closest.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}