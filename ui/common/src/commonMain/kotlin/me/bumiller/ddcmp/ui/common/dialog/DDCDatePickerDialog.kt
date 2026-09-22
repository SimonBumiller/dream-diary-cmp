package me.bumiller.ddcmp.ui.common.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.date_picker_dialog_cancel_label
import dream_diary_cmp.ui.common.generated.resources.date_picker_dialog_confirm_label
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun SelectablePolicy(now: Long) = object : SelectableDates {

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis <= now
    }

}

@OptIn(ExperimentalTime::class)
@Composable
fun DDCDatePickerDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSelect: (LocalDate, Instant) -> Unit,
    initialSelected: Instant
) {
    val state = rememberDatePickerState(
        initialSelectedDateMillis = initialSelected.toEpochMilliseconds(),
        selectableDates = SelectablePolicy(Clock.System.now().toEpochMilliseconds())
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val selected = state.selectedDateMillis
                    if(selected == null) onDismiss()
                    else {
                        val instant = Instant.fromEpochMilliseconds(selected)
                        val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                        onSelect(date, instant)
                    }
                },
                enabled = state.selectedDateMillis != null
            ) {
                Text(stringResource(Res.string.date_picker_dialog_confirm_label))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(Res.string.date_picker_dialog_cancel_label))
            }
        }
    ) {
        DatePicker(
            modifier = Modifier
                .fillMaxWidth(),
            state = state
        )
    }
}