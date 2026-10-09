package me.ddcmp.feature.io.entries.import_result

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.io.generated.resources.Res
import dream_diary_cmp.feature.io.generated.resources.import_result_continue_label_success
import dream_diary_cmp.feature.io.generated.resources.import_result_description_failure_access
import dream_diary_cmp.feature.io.generated.resources.import_result_description_failure_consistency
import dream_diary_cmp.feature.io.generated.resources.import_result_description_failure_format
import dream_diary_cmp.feature.io.generated.resources.import_result_description_success_legacy
import dream_diary_cmp.feature.io.generated.resources.import_result_description_success_normal
import dream_diary_cmp.feature.io.generated.resources.import_result_title_failure
import dream_diary_cmp.feature.io.generated.resources.import_result_title_success
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.io.ImportResult
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.format.formatInstantMonthYear
import me.bumiller.ddcmp.ui.common.text.styledText
import me.ddcmp.feature.io.entries.import_result.ImportResultViewModel.UiEvent
import me.ddcmp.feature.io.entries.import_result.ImportResultViewModel.ViewModelEvent
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
internal fun ImportResultEntry(
    onContinue: (wasSuccess: Boolean) -> Unit
) {
    ViewModelScope<UiEvent, ViewModelEvent, ImportResultViewModel>(
        onViewModelEvent = {
            when (it) {
                is ViewModelEvent.Continue -> onContinue(it.wasSuccess)
            }
        }
    ) {
        val result by it.result.collectAsStateWithLifecycle()

        if (result != null) {
            ImportResultEntry(it::onEvent, result!!)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImportResultEntry(
    onEvent: (UiEvent) -> Unit,
    result: ImportResult
) {
    AlertDialog(
        onDismissRequest = { onEvent(UiEvent.Continue(result is ImportResult.Success)) },
        confirmButton = {
            TextButton(
                onClick = { onEvent(UiEvent.Continue(result is ImportResult.Success)) }
            ) {
                Text(stringResource(Res.string.import_result_continue_label_success))
            }
        },
        title = {
            val resource = when (result) {
                is ImportResult.Failure -> Res.string.import_result_title_failure
                is ImportResult.Success -> Res.string.import_result_title_success
            }
            Text(stringResource(resource))
        },
        text = {
            when (result) {
                is ImportResult.Failure -> DialogTextFailure(result)
                is ImportResult.Success -> DialogTextSuccess(result)
            }
        }
    )
}

@Composable
private fun DialogTextFailure(
    result: ImportResult.Failure
) {
    val resource = when (result) {
        is ImportResult.Failure.DataConsistency -> Res.string.import_result_description_failure_consistency
        is ImportResult.Failure.FileAccess -> Res.string.import_result_description_failure_access
        is ImportResult.Failure.FileFormat -> Res.string.import_result_description_failure_format
    }

    Text(stringResource(resource))
}

@OptIn(ExperimentalTime::class)
@Composable
private fun DialogTextSuccess(
    result: ImportResult.Success
) {
    val text = when (result) {
        is ImportResult.Success.Legacy -> stringResource(
            Res.string.import_result_description_success_legacy,
            result.dreamCount,
            result.personCount,
            formatInstantMonthYear(result.firstDream),
            formatInstantMonthYear(result.lastDream),
            monthsBetween(result.firstDream, result.lastDream)
        )

        is ImportResult.Success.Normal -> stringResource(
            Res.string.import_result_description_success_normal,
            result.dreamCount,
            result.personCount,
            result.emotionCount,
            formatInstantMonthYear(result.firstDream),
            formatInstantMonthYear(result.lastDream),
            monthsBetween(result.firstDream, result.lastDream)
        )
    }

    val spannedString = styledText(text)

    Text(spannedString)
}

@OptIn(ExperimentalTime::class)
private fun monthsBetween(
    start: Instant,
    end: Instant,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): Int {
    val startDate = start.toLocalDateTime(timeZone).date
    val endDate = end.toLocalDateTime(timeZone).date

    return (endDate.year - startDate.year) * 12 +
            (endDate.month.number - startDate.month.number)
}