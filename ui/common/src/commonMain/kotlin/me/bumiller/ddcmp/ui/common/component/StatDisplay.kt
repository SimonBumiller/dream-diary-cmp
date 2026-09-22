package me.bumiller.ddcmp.ui.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.stat_data_missing_description
import me.bumiller.ddcmp.domain.model.stats.Stat
import me.bumiller.ddcmp.ui.common.icons.database_off
import me.bumiller.ddcmp.ui.common.style.DisabledContent
import org.jetbrains.compose.resources.stringResource

@Composable
fun <T> StatCard(
    modifier: Modifier = Modifier,
    stat: Stat<T>,
    title: String,
    description: String,
    badges: (@Composable RowScope.(T) -> Unit)? = null,
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
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                        .copy(fontWeight = FontWeight.Bold)
                )

                Spacer(
                    modifier = Modifier
                        .weight(1F)
                )

                if(badges != null && stat is Stat.Present) {
                    badges(stat.stat)
                }
            }
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
            DisabledContent {
                Column(
                    modifier = modifier
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(48.dp),
                        imageVector = database_off,
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(Res.string.stat_data_missing_description),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
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