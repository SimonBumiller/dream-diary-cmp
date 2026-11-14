package me.ddcmp.feature.home.entries.taglist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.home.generated.resources.Res
import dream_diary_cmp.feature.home.generated.resources.tag_list_add
import dream_diary_cmp.feature.home.generated.resources.tag_list_description
import dream_diary_cmp.feature.home.generated.resources.tag_list_empty_placeholder
import dream_diary_cmp.feature.home.generated.resources.tag_list_long_press_label
import dream_diary_cmp.feature.home.generated.resources.tag_list_target_dream
import dream_diary_cmp.feature.home.generated.resources.tag_list_target_person
import dream_diary_cmp.feature.home.generated.resources.tag_list_title
import me.bumiller.ddcmp.domain.usecase.tag.AllTagUseCase
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.component.AppTagInputChip
import me.bumiller.ddcmp.ui.common.component.BackIconButton
import me.bumiller.ddcmp.ui.common.layout.AppBarAction
import me.bumiller.ddcmp.ui.common.layout.AppBarActions
import me.bumiller.ddcmp.ui.common.plus
import me.bumiller.ddcmp.ui.common.style.DisabledContent
import me.ddcmp.feature.home.entries.taglist.TagListViewModel.UiEvent
import me.ddcmp.feature.home.entries.taglist.TagListViewModel.ViewModelEvent
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TagListEntry(
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onEdit: (Long) -> Unit
) {
    ViewModelScope<UiEvent, ViewModelEvent, TagListViewModel>(
        onViewModelEvent = {
            when (it) {
                ViewModelEvent.NavigateAdd -> onAdd()
                ViewModelEvent.NavigateBack -> onBack()
                is ViewModelEvent.NavigateEdit -> onEdit(it.tagId)
            }
        }
    ) { vm ->
        val personTags by vm.tagsPerson.collectAsStateWithLifecycle()
        val dreamTags by vm.tagsDream.collectAsStateWithLifecycle()

        if(personTags != null && dreamTags != null) {
            TagListEntry(personTags!!, dreamTags!!, vm::onEvent)
        }
    }
}

@Composable
private fun TagListEntry(
    personTags: List<AllTagUseCase.O>,
    dreamTags: List<AllTagUseCase.O>,
    onEvent: (UiEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(Res.string.tag_list_title)) },
                navigationIcon = { BackIconButton { onEvent(UiEvent.ClickOnBack) } },
                actions = {
                    AppBarActions(
                        actions = listOf(
                            AppBarAction(
                                label = stringResource(Res.string.tag_list_add),
                                icon = Icons.Default.Add,
                                handler = { onEvent(UiEvent.ClickOnAdd) }
                            )
                        )
                    )
                }
            )
        }
    ) { pv ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv + PaddingValues(horizontal = 12.dp)),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val bothEmpty = personTags.isEmpty() && dreamTags.isEmpty()

            val msg = if (bothEmpty) Res.string.tag_list_empty_placeholder
            else Res.string.tag_list_description

            DisabledContent(personTags.isEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(msg),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if(!bothEmpty) {
                Text(
                    modifier = Modifier
                        .align(Alignment.End),
                    text = stringResource(Res.string.tag_list_long_press_label),
                    style = MaterialTheme.typography.labelSmall
                )
            }

            if(personTags.isNotEmpty()) {
                TagList(
                    title = stringResource(Res.string.tag_list_target_person),
                    tags = personTags,
                    onEvent = onEvent
                )
            }

            if(dreamTags.isNotEmpty()) {
                TagList(
                    title = stringResource(Res.string.tag_list_target_dream),
                    tags = dreamTags,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
private fun TagList(
    title: String,
    tags: List<AllTagUseCase.O>,
    onEvent: (UiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        FlowRow(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            tags.forEach { tag ->
                AppTagInputChip(
                    modifier = Modifier
                        .padding(horizontal = 6.dp),
                    tag = tag.tag,
                    onClick = { },
                    onLongClick = { onEvent(UiEvent.ClickEditTag(tag.tag.id)) },
                    selected = false
                )
            }
        }
    }

}