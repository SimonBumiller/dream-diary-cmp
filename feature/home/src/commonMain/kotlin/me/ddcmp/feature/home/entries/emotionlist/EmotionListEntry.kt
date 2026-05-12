package me.ddcmp.feature.home.entries.emotionlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.home.generated.resources.Res
import dream_diary_cmp.feature.home.generated.resources.emotionlist_empty_placeholder_clickable
import dream_diary_cmp.feature.home.generated.resources.emotionlist_empty_placeholder_post
import dream_diary_cmp.feature.home.generated.resources.emotionlist_empty_placeholder_pre
import dream_diary_cmp.feature.home.generated.resources.emotionlist_matrix_description
import dream_diary_cmp.feature.home.generated.resources.emotionlist_matrix_title
import dream_diary_cmp.feature.home.generated.resources.emotionlist_title
import me.bumiller.ddcmp.domain.model.Emotion
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.component.EmotionGrid
import me.bumiller.ddcmp.ui.common.component.MultiStyleText
import me.bumiller.ddcmp.ui.common.component.TextStyleWithCallback
import me.bumiller.ddcmp.ui.common.plus
import me.bumiller.ddcmp.ui.common.style.DisabledContent
import me.ddcmp.feature.home.component.ChipEmotionBadge
import me.ddcmp.feature.home.entries.emotionlist.EmotionListViewModel.UiEvent
import me.ddcmp.feature.home.entries.emotionlist.EmotionListViewModel.UiEvent.AddBasicEmotions
import me.ddcmp.feature.home.entries.emotionlist.EmotionListViewModel.UiEvent.AddEmotion
import me.ddcmp.feature.home.entries.emotionlist.EmotionListViewModel.UiState
import me.ddcmp.feature.home.entries.emotionlist.EmotionListViewModel.ViewModelEvent
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun EmotionListEntry(
    onBack: () -> Unit,
    onAdd: () -> Unit
) {
    ViewModelScope<UiEvent, ViewModelEvent, EmotionListViewModel>(
        onViewModelEvent = {
            when (it) {
                ViewModelEvent.NavigateBack -> onBack()
                ViewModelEvent.NavigateToAdd -> onAdd()
            }
        }
    ) { vm ->
        val uiState by vm.uiState.collectAsStateWithLifecycle()
        val emotions by vm.emotions.collectAsStateWithLifecycle()

        EmotionListEntry(vm::onEvent, uiState, emotions)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmotionListEntry(
    onEvent: (UiEvent) -> Unit,
    uiState: UiState,
    emotions: List<Emotion>
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(AddEmotion)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(Res.string.emotionlist_title)) },
                navigationIcon = {
                    IconButton(
                        onClick = { onEvent(UiEvent.GoBack) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
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
            if (emotions.isEmpty()) {
                DisabledContent {
                    val normalStyle = MaterialTheme.typography.bodySmall
                        .let {
                            TextStyleWithCallback(
                                style = it,
                                callback = null
                            )
                        }

                    val clickableStyle = MaterialTheme.typography.bodySmall
                        .copy(color = LocalContentColor.current)
                        .let {
                            TextStyleWithCallback(
                                style = it,
                                callback = { onEvent(AddBasicEmotions) }
                            )
                        }

                    MultiStyleText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodySmall
                            .copy(textAlign = TextAlign.Center),
                        stringResource(Res.string.emotionlist_empty_placeholder_pre) to normalStyle,
                        stringResource(Res.string.emotionlist_empty_placeholder_clickable) to clickableStyle,
                        stringResource(Res.string.emotionlist_empty_placeholder_post) to normalStyle
                    )
                }
            }

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                emotions.forEach { emotion ->
                    ChipEmotionBadge(
                        modifier = Modifier
                            .padding(horizontal = 6.dp),
                        emotions = listOf(emotion),
                        label = { it.name },
                        onClick = {},
                        selected = emotion.id == uiState.selectedEmotion?.id
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(Res.string.emotionlist_matrix_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(Res.string.emotionlist_matrix_description),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            EmotionGrid(
                modifier = Modifier
                    .fillMaxWidth(),
                existingPoints = emotions.toSet(),
                onClickClosest = {
                    onEvent(UiEvent.SelectEmotion(it))
                }
            )
        }
    }
}