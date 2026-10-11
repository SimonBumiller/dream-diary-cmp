package me.ddcmp.feature.settings.entries.order_detail.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import dream_diary_cmp.feature.settings.generated.resources.Res
import dream_diary_cmp.feature.settings.generated.resources.export_failure_access_description
import dream_diary_cmp.feature.settings.generated.resources.export_failure_confirm
import dream_diary_cmp.feature.settings.generated.resources.export_failure_title
import me.bumiller.ddcmp.domain.model.io.ExportResult
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ExportFailureDialog(
    exportFailure: ExportResult.Failure,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) { Text(stringResource(Res.string.export_failure_confirm)) }
        },
        title = { Text(stringResource(Res.string.export_failure_title)) },
        text = {
            val description = when (exportFailure) {
                is ExportResult.Failure.FileAccess -> Res.string.export_failure_access_description
            }

            Text(stringResource(description))
        }
    )
}