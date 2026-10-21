package me.ddcmp.feature.home.entries.dreamadd.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import dream_diary_cmp.feature.home.generated.resources.Res
import dream_diary_cmp.feature.home.generated.resources.attachment_add_confirm
import dream_diary_cmp.feature.home.generated.resources.attachment_add_dismiss
import dream_diary_cmp.feature.home.generated.resources.attachment_add_name_label
import dream_diary_cmp.feature.home.generated.resources.attachment_add_name_placeholder
import dream_diary_cmp.feature.home.generated.resources.attachment_add_title
import dream_diary_cmp.feature.home.generated.resources.dreamadd_attachmentadd_description
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.name
import me.bumiller.ddcmp.ui.common.text.styledText
import me.ddcmp.feature.home.entries.dreamadd.DreamAddViewModel.ViewModelAttachment
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AddAttachmentDialog(
    file: PlatformFile,
    onSave: (ViewModelAttachment.Draft?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onSave(null) },
        confirmButton = {
            TextButton(
                onClick = {
                    val draft = ViewModelAttachment.Draft(name, description, file)
                    onSave(draft)
                },
                enabled = name.isNotBlank()
            ) { Text(stringResource(Res.string.attachment_add_confirm)) }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onSave(null)
                }
            ) { Text(stringResource(Res.string.attachment_add_dismiss)) }
        },
        title = { Text(stringResource(Res.string.attachment_add_title)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.dreamadd_attachmentadd_description, file.name)
                        .let(::styledText)
                )

                HorizontalDivider()

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    placeholder = { Text(stringResource(Res.string.attachment_add_name_placeholder)) },
                    label = { Text(stringResource(Res.string.attachment_add_name_label)) }
                )
            }
        }
    )
}