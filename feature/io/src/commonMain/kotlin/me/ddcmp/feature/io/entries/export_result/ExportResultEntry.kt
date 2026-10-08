package me.ddcmp.feature.io.entries.export_result

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.io.generated.resources.Res
import dream_diary_cmp.feature.io.generated.resources.export_result_continue_label_success
import dream_diary_cmp.feature.io.generated.resources.export_result_description
import dream_diary_cmp.feature.io.generated.resources.export_result_description_access
import dream_diary_cmp.feature.io.generated.resources.export_result_title_failure
import dream_diary_cmp.feature.io.generated.resources.export_result_title_success
import me.bumiller.ddcmp.domain.model.io.ExportResult
import me.bumiller.ddcmp.domain.model.io.ImportResult
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.text.styledText
import me.ddcmp.feature.io.entries.export_result.ExportResultViewModel.UiEvent
import me.ddcmp.feature.io.entries.export_result.ExportResultViewModel.ViewModelEvent
import me.ddcmp.feature.io.entries.export_result.ExportResultViewModel.ViewModelEvent.Continue
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

@Composable
internal fun ExportResultEntry(
    onContinue: (wasSuccess: Boolean) -> Unit
) {
    ViewModelScope<UiEvent, ViewModelEvent, ExportResultViewModel>(
        onViewModelEvent = {
            when (it) {
                is Continue -> onContinue(it.wasSuccess)
            }
        }
    ) {
        val result by it.result.collectAsStateWithLifecycle()

        if (result != null) {
            ExportResultEntry(it::onEvent, result!!)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExportResultEntry(
    onEvent: (UiEvent) -> Unit,
    result: ExportResult
) {
    AlertDialog(
        onDismissRequest = { onEvent(UiEvent.Continue(result is ImportResult.Success)) },
        confirmButton = {
            TextButton(
                onClick = { onEvent(UiEvent.Continue(result is ImportResult.Success)) }
            ) {
                Text(stringResource(Res.string.export_result_continue_label_success))
            }
        },
        title = {
            val resource = when (result) {
                is ExportResult.Failure -> Res.string.export_result_title_failure
                is ExportResult.Success -> Res.string.export_result_title_success
            }
            Text(stringResource(resource))
        },
        text = {
            when (result) {
                is ExportResult.Failure -> DialogTextFailure(result)
                is ExportResult.Success -> DialogTextSuccess(result)
            }
        }
    )
}

@Composable
private fun DialogTextFailure(
    result: ExportResult.Failure
) {
    val resource = when (result) {
        is ExportResult.Failure.FileAccess -> Res.string.export_result_description_access
    }

    Text(stringResource(resource))
}

@OptIn(ExperimentalTime::class)
@Composable
private fun DialogTextSuccess(
    result: ExportResult.Success
) {
    val (count, unit) =
        when {
            result.fileSize < 1_000L -> result.fileSize to "B"
            result.fileSize < 1_000_000L -> result.fileSize / 1_000L to "KB"
            result.fileSize < 1_000_000_000L -> result.fileSize / 1_000_000L to "MB"
            else -> result.fileSize / 1_000_000_000L to "GB"
        }

    val text = stringResource(
        Res.string.export_result_description,
        result.fileLocation,
        count,
        unit
    )

    val spannedString = styledText(text)

    Text(spannedString)
}