package me.bumiller.ddcmp.ui.common.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.date_range_dialog_description
import dream_diary_cmp.ui.common.generated.resources.date_range_dialog_preconfigured_all_time
import dream_diary_cmp.ui.common.generated.resources.date_range_dialog_preconfigured_last_five_years
import dream_diary_cmp.ui.common.generated.resources.date_range_dialog_preconfigured_last_half_year
import dream_diary_cmp.ui.common.generated.resources.date_range_dialog_preconfigured_last_month
import dream_diary_cmp.ui.common.generated.resources.date_range_dialog_preconfigured_last_two_years
import dream_diary_cmp.ui.common.generated.resources.date_range_dialog_preconfigured_last_week
import dream_diary_cmp.ui.common.generated.resources.date_range_dialog_preconfigured_last_year
import kotlinx.datetime.DatePeriod
import org.jetbrains.compose.resources.stringResource

/**
 * A date range that was picked by the user.
 */
sealed interface PickedDateRange {

    /**
     * Current date is offset by a specific period.
     */
    data class Offset(val offset: DatePeriod) : PickedDateRange

    /**
     * Whole time is taken into account.
     */
    data object AllTime : PickedDateRange

}

private val PreconfiguredRanges = listOf(
    Res.string.date_range_dialog_preconfigured_last_week to DatePeriod(days = 7),
    Res.string.date_range_dialog_preconfigured_last_month to DatePeriod(days = 30),
    Res.string.date_range_dialog_preconfigured_last_half_year to DatePeriod(months = 6),
    Res.string.date_range_dialog_preconfigured_last_year to DatePeriod(years = 1),
    Res.string.date_range_dialog_preconfigured_last_two_years to DatePeriod(years = 2),
    Res.string.date_range_dialog_preconfigured_last_five_years to DatePeriod(years = 5)
)

@Composable
fun DateRangeDialog(
    modifier: Modifier,
    onDismiss: () -> Unit,
    onRangeSelected: (PickedDateRange?) -> Unit,
    currentRange: PickedDateRange
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text("Time Duration")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(stringResource(Res.string.date_range_dialog_description))

                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth(),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        count = PreconfiguredRanges.size
                    ) { index ->
                        val (name, period) = PreconfiguredRanges[index]

                        RangeButton(
                            modifier = Modifier
                                .weight(1F)
                                .height(64.dp),
                            text = stringResource(name),
                            onClick = {
                                onRangeSelected(PickedDateRange.Offset(period))
                                onDismiss()
                            },
                            selected = (currentRange as? PickedDateRange.Offset)?.offset == period
                        )
                    }

                    item(
                        span = { GridItemSpan(maxLineSpan) }
                    ) {
                        RangeButton(
                            modifier = Modifier
                                .weight(1F)
                                .height(64.dp),
                            text = stringResource(Res.string.date_range_dialog_preconfigured_all_time),
                            onClick = {
                                onRangeSelected(PickedDateRange.AllTime)
                                onDismiss()
                            },
                            selected = currentRange == PickedDateRange.AllTime
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Save")
            }
        }
    )
}

@Composable
private fun RangeButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    selected: Boolean
) {
    if (selected) {
        OutlinedCard(
            modifier = modifier,
            onClick = onClick
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text(text) }
        }
    } else {
        Card(
            modifier = modifier,
            onClick = onClick
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text(text) }
        }
    }
}