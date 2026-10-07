package me.ddcmp.feature.io.entries.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.io.generated.resources.Res
import dream_diary_cmp.feature.io.generated.resources.result_continue_label_success
import dream_diary_cmp.feature.io.generated.resources.result_description_success_legacy
import dream_diary_cmp.feature.io.generated.resources.result_description_success_normal
import dream_diary_cmp.feature.io.generated.resources.result_title_failure
import dream_diary_cmp.feature.io.generated.resources.result_title_success
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.ImportResult
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.format.formatInstantMonthYear
import me.bumiller.ddcmp.ui.common.plus
import me.bumiller.ddcmp.ui.common.text.styledText
import me.ddcmp.feature.io.entries.result.ImportResultViewModel.UiEvent
import me.ddcmp.feature.io.entries.result.ImportResultViewModel.ViewModelEvent
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
internal fun ImportResultEntry(
    onContinue: (wasSuccess: Boolean) -> Unit
) {
    println("Inside ResultEntry")
    ViewModelScope<UiEvent, ViewModelEvent, ImportResultViewModel>(
        onViewModelEvent = {
            when (it) {
                is ViewModelEvent.Continue -> onContinue(it.wasSuccess)
            }
        }
    ) {
        val result by it.result.collectAsStateWithLifecycle()

        println("Got result $result")

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
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    val text = if (result is ImportResult.Success) Res.string.result_title_success
                    else Res.string.result_title_failure
                    Text(stringResource(text))
                }
            )
        }
    ) { pv ->
        if (result is ImportResult.Success) {
            ResultContentSuccess(
                modifier = Modifier
                    .padding(pv + PaddingValues(horizontal = 16.dp))
                    .fillMaxSize(),
                result = result,
                onReturn = { onEvent(UiEvent.Continue(true)) }
            )
        } else {
            Box(
                modifier = Modifier
                    .padding(pv + PaddingValues(horizontal = 16.dp))
            ) {
                // TODO: Here we should explicitly state why the importing did not succeed.
                Text("Failure")
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun ResultContentSuccess(
    modifier: Modifier = Modifier,
    result: ImportResult.Success,
    onReturn: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement
            .spacedBy(16.dp)
    ) {
        val text = if (result is ImportResult.Success.Normal)
            stringResource(
                Res.string.result_description_success_normal,
                result.dreamCount,
                result.personCount,
                result.emotionCount,
                formatInstantMonthYear(result.firstDream),
                formatInstantMonthYear(result.lastDream),
                monthsBetween(result.firstDream, result.lastDream)
            )
        else stringResource(
            Res.string.result_description_success_legacy,
            result.dreamCount,
            result.personCount,
            formatInstantMonthYear(result.firstDream),
            formatInstantMonthYear(result.lastDream),
            monthsBetween(result.firstDream, result.lastDream)
        )

        val spannedString = styledText(text)

        Text(
            text = spannedString,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Button(
            onClick = onReturn
        ) {
            Text(stringResource(Res.string.result_continue_label_success))
        }
    }
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