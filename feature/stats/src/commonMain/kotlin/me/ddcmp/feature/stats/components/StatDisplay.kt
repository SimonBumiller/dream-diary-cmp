package me.ddcmp.feature.stats.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dream_diary_cmp.feature.stats.generated.resources.Res
import dream_diary_cmp.feature.stats.generated.resources.stat_data_missing_description
import dream_diary_cmp.feature.stats.generated.resources.stat_data_missing_title
import me.bumiller.ddcmp.domain.model.stats.Stat
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun <T> StatCard(
    modifier: Modifier = Modifier,
    stat: Stat<T>,
    title: String,
    description: String,
    display: @Composable (T) -> Unit
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 6.dp,
                    end = 6.dp,
                    bottom = 6.dp
                )
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
                    .copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
            ) {
                StatDisplay(
                    stat = stat,
                    display = display
                )
            }
        }
    }
}

@Composable
internal fun <T> StatDisplay(
    modifier: Modifier = Modifier,
    stat: Stat<T>,
    display: @Composable (T) -> Unit
) {
    when (stat) {
        is Stat.NotEnoughData -> {
            Column(
                modifier = modifier
                    .height(300.dp)
            ) {
                Text(
                    text = stringResource(Res.string.stat_data_missing_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(Res.string.stat_data_missing_description),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        is Stat.Present -> {
            Box(
                modifier = modifier
            ) {
                display(stat.stat)
            }
        }
    }
}