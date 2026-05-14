package me.ddcmp.feature.home.entries.personadd

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.home.generated.resources.Res
import dream_diary_cmp.feature.home.generated.resources.personadd_cancel_label
import dream_diary_cmp.feature.home.generated.resources.personadd_confirm_label_add
import dream_diary_cmp.feature.home.generated.resources.personadd_confirm_label_edit
import dream_diary_cmp.feature.home.generated.resources.personadd_name_label
import dream_diary_cmp.feature.home.generated.resources.personadd_name_placeholder
import dream_diary_cmp.feature.home.generated.resources.personadd_note_label
import dream_diary_cmp.feature.home.generated.resources.personadd_note_placeholder
import dream_diary_cmp.feature.home.generated.resources.personadd_title_add
import dream_diary_cmp.feature.home.generated.resources.personadd_title_edit
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.ddcmp.feature.home.entries.personadd.PersonAddViewModel.PersonState
import me.ddcmp.feature.home.entries.personadd.PersonAddViewModel.UiEvent
import me.ddcmp.feature.home.entries.personadd.PersonAddViewModel.ViewModelEvent
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PersonAddEntry(
    onCloseDialog: () -> Unit
) {
    ViewModelScope<UiEvent, ViewModelEvent, PersonAddViewModel>(
        onViewModelEvent = {
            when (it) {
                ViewModelEvent.CloseDialog -> onCloseDialog()
            }
        }
    ) { vm ->
        val personState by vm.personState.collectAsStateWithLifecycle()
        val canSave by vm.canSave.collectAsStateWithLifecycle()
        val isAdd = vm.isAdd

        PersonEntry(vm::onEvent, personState, canSave, isAdd)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersonEntry(
    onEvent: (UiEvent) -> Unit,
    personState: PersonState,
    canSave: Boolean,
    isAdd: Boolean
) {
    AlertDialog(
        onDismissRequest = { onEvent(UiEvent.Cancel) },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        title = {
            val msg = if(isAdd) Res.string.personadd_title_add
                else Res.string.personadd_title_edit
            Text(stringResource(msg))
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = personState.name,
                    onValueChange = { onEvent(UiEvent.ChangeName(it)) },
                    singleLine = true,
                    placeholder = { Text(stringResource(Res.string.personadd_name_placeholder)) },
                    label = { Text(stringResource(Res.string.personadd_name_label)) }
                )
                OutlinedTextField(
                    value = personState.note,
                    onValueChange = { onEvent(UiEvent.ChangeNote(it)) },
                    placeholder = { Text(stringResource(Res.string.personadd_note_placeholder)) },
                    label = { Text(stringResource(Res.string.personadd_note_label)) },
                    minLines = 3
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onEvent(UiEvent.Save) },
                enabled = canSave
            ) {
                val msg = if(isAdd) Res.string.personadd_confirm_label_add
                    else Res.string.personadd_confirm_label_edit
                Text(stringResource(msg))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onEvent(UiEvent.Cancel) }
            ) {
                Text(stringResource(Res.string.personadd_cancel_label))
            }
        }
    )
}