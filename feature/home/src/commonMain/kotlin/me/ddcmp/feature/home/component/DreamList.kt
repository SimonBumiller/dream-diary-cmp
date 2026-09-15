package me.ddcmp.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dream_diary_cmp.feature.home.generated.resources.Res
import dream_diary_cmp.feature.home.generated.resources.dreamlist_empty_placeholder
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.ui.common.style.DisabledContent
import me.ddcmp.feature.home.entries.dreamlist.DreamListViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DreamLazyColumn(
    modifier: Modifier = Modifier,
    dreams: List<AllDreamUseCase.O>,
    onClick: (Dream) -> Unit,
    selectedId: Long? = null,
    emptyPlaceholder: @Composable () -> String = {
        stringResource(Res.string.dreamlist_empty_placeholder)
    }
) {
    val isEmpty = dreams.isEmpty()
    val hasDreams = dreams.isNotEmpty()

    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isEmpty) {
            item {
                DisabledContent {
                    Text(
                        text = emptyPlaceholder(),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (hasDreams) {
            items(
                count = dreams.size,
                key = { dreams[it].dream.id }
            ) {
                DreamItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    dream = dreams[it].dream,
                    onClick = { onClick(dreams[it].dream) },
                    selected = dreams[it].dream.id == selectedId,
                    emotions = dreams[it].emotions,
                    persons = dreams[it].persons
                )
            }
        }

    }
}