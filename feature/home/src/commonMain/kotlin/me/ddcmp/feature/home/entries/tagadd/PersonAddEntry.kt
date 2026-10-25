package me.ddcmp.feature.home.entries.tagadd

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.home.generated.resources.Res
import dream_diary_cmp.feature.home.generated.resources.tag_add_cancel_label
import dream_diary_cmp.feature.home.generated.resources.tag_add_confirm_label_add
import dream_diary_cmp.feature.home.generated.resources.tag_add_confirm_label_edit
import dream_diary_cmp.feature.home.generated.resources.tag_add_name_label
import dream_diary_cmp.feature.home.generated.resources.tag_add_name_placeholder
import dream_diary_cmp.feature.home.generated.resources.tag_add_note_label
import dream_diary_cmp.feature.home.generated.resources.tag_add_note_placeholder
import dream_diary_cmp.feature.home.generated.resources.tag_add_title_add
import dream_diary_cmp.feature.home.generated.resources.tag_add_title_edit
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.component.ColorPicker
import me.ddcmp.feature.home.entries.tagadd.TagAddViewModel.TagState
import me.ddcmp.feature.home.entries.tagadd.TagAddViewModel.UiEvent
import me.ddcmp.feature.home.entries.tagadd.TagAddViewModel.ViewModelEvent
import me.ddcmp.feature.home.entries.tagadd.TagAddViewModel.ViewModelEvent.CloseDialog
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TagAddEntry(
    onCloseDialog: () -> Unit
) {
    ViewModelScope<UiEvent, ViewModelEvent, TagAddViewModel>(
        onViewModelEvent = {
            when (it) {
                CloseDialog -> onCloseDialog()
            }
        }
    ) { vm ->
        val tagState by vm.tagState.collectAsStateWithLifecycle()
        val canSave by vm.canSave.collectAsStateWithLifecycle()
        val isAdd = vm.isAdd

        TagAddEntry(vm::onEvent, tagState, canSave, isAdd)
    }
}

@Composable
private fun TagAddEntry(
    onEvent: (UiEvent) -> Unit,
    tagState: TagState,
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
            val msg = if(isAdd) Res.string.tag_add_title_add
                else Res.string.tag_add_title_edit
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(msg))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if(!isAdd) {
                        IconButton(
                            onClick = { onEvent(UiEvent.ClickDelete) }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null
                            )
                        }
                    }

                    ColorPicker(
                        selectedColor = tagState.color,
                        onColorChange = {
                            onEvent(UiEvent.ChangeColor(it))
                        }
                    )
                }
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = tagState.name,
                    onValueChange = { onEvent(UiEvent.ChangeName(it)) },
                    singleLine = true,
                    placeholder = { Text(stringResource(Res.string.tag_add_name_placeholder)) },
                    label = { Text(stringResource(Res.string.tag_add_name_label)) }
                )
                OutlinedTextField(
                    value = tagState.description,
                    onValueChange = { onEvent(UiEvent.ChangeDescription(it)) },
                    placeholder = { Text(stringResource(Res.string.tag_add_note_placeholder)) },
                    label = { Text(stringResource(Res.string.tag_add_note_label)) },
                    minLines = 3
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onEvent(UiEvent.Save) },
                enabled = canSave
            ) {
                val msg = if(isAdd) Res.string.tag_add_confirm_label_add
                    else Res.string.tag_add_confirm_label_edit
                Text(stringResource(msg))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onEvent(UiEvent.Cancel) }
            ) {
                Text(stringResource(Res.string.tag_add_cancel_label))
            }
        }
    )
}