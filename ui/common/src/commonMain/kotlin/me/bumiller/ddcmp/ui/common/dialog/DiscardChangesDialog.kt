package me.bumiller.ddcmp.ui.common.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.discard_changes_dialog_confirm_label
import dream_diary_cmp.ui.common.generated.resources.discard_changes_dialog_dismiss_label
import dream_diary_cmp.ui.common.generated.resources.discard_changes_dialog_text
import dream_diary_cmp.ui.common.generated.resources.discard_changes_dialog_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun DiscardChangesDialog(
    modifier: Modifier = Modifier,
    onDiscard: () -> Unit,
    onKeep: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onKeep,
        confirmButton = {
            TextButton(
                onClick = onDiscard
            ) {
                Text(stringResource(Res.string.discard_changes_dialog_confirm_label))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onKeep
            ) {
                Text(stringResource(Res.string.discard_changes_dialog_dismiss_label))
            }
        },
        title = { Text(stringResource(Res.string.discard_changes_dialog_title)) },
        text = { Text(stringResource(Res.string.discard_changes_dialog_text)) }
    )
}