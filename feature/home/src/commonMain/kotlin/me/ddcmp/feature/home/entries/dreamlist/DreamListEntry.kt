package me.ddcmp.feature.home.entries.dreamlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.home.generated.resources.Res
import dream_diary_cmp.feature.home.generated.resources.dreamlist_action_add
import dream_diary_cmp.feature.home.generated.resources.dreamlist_title
import me.bumiller.ddcmp.domain.model.meta.SortDirection
import me.bumiller.ddcmp.domain.model.meta.SortMode.DreamSort
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.layout.AppBarAction
import me.bumiller.ddcmp.ui.common.layout.AppBarActions
import me.bumiller.ddcmp.ui.common.layout.TopBarWithProgressIndicator
import me.bumiller.ddcmp.ui.common.translation.translateName
import me.ddcmp.feature.home.component.DreamLazyColumn
import me.ddcmp.feature.home.entries.dreamlist.DreamListViewModel.UiEvent
import me.ddcmp.feature.home.entries.dreamlist.DreamListViewModel.ViewModelEvent
import me.ddcmp.feature.home.entries.dreamlist.DreamListViewModel.ViewModelEvent.GoToAddDream
import me.ddcmp.feature.home.entries.dreamlist.DreamListViewModel.ViewModelEvent.GoToDreamDetail
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DreamListEntry(
    onAddDream: () -> Unit,
    onGoToDreamDetail: (id: Long) -> Unit,
    goBack: () -> Unit
) {
    ViewModelScope<UiEvent, ViewModelEvent, DreamListViewModel>(
        onViewModelEvent = {
            when (it) {
                GoToAddDream -> onAddDream()
                is GoToDreamDetail -> onGoToDreamDetail(it.dreamId)
                ViewModelEvent.GoBack -> goBack()
            }
        }
    ) { vm ->
        val dreams by vm.dreamState.collectAsStateWithLifecycle()
        val uiState by vm.uiState.collectAsStateWithLifecycle()

        DreamListEntry(vm::onEvent, dreams, uiState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DreamListEntry(
    onEvent: (UiEvent) -> Unit,
    dreamState: List<AllDreamUseCase.O>?,
    uiState: DreamListViewModel.UiState
) {
    val isLoading = dreamState == null
    val hasDreams = dreamState?.isNotEmpty() == true

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBarWithProgressIndicator(
                loading = isLoading
            ) {
                CenterAlignedTopAppBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = {
                        Text(
                            text = stringResource(Res.string.dreamlist_title)
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { onEvent(UiEvent.GoBack) }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        AppBarActions(
                            actions = listOf(
                                AppBarAction(
                                    label = stringResource(Res.string.dreamlist_action_add),
                                    icon = Icons.Default.Add,
                                    handler = { onEvent(UiEvent.AddDream) }
                                )
                            )
                        )
                    }
                )
            }
        }
    ) { pv ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
        ) {
            if (hasDreams) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Box {
                        Text(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .clickable(
                                    onClick = {
                                        onEvent(UiEvent.ToggleDropdownVisibility(!uiState.sortModeDropdownVisible))
                                    }
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            text = uiState.sortMode.translateName,
                            style = MaterialTheme.typography.labelLarge
                        )

                        DropdownMenu(
                            expanded = uiState.sortModeDropdownVisible,
                            onDismissRequest = { onEvent(UiEvent.ToggleDropdownVisibility(false)) }
                        ) {
                            listOf(
                                DreamSort.Dreamt,
                                DreamSort.Name,
                                DreamSort.Length,
                                DreamSort.AverageValence,
                                DreamSort.Confidentiality
                            ).forEach { mode ->
                                DropdownMenuItem(
                                    text = { Text(mode.translateName) },
                                    onClick = {
                                        onEvent(UiEvent.ToggleDropdownVisibility(false))
                                        onEvent(UiEvent.ChangeSortMode(mode))
                                    }
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = { onEvent(UiEvent.ChangeSortDirection(!uiState.sortDirection)) }
                    ) {
                        Icon(
                            imageVector = when (uiState.sortDirection) {
                                SortDirection.Ascending -> Icons.Default.ArrowUpward
                                SortDirection.Descending -> Icons.Default.ArrowDownward
                            },
                            contentDescription = uiState.sortDirection.translateName
                        )
                    }
                }
            }

            if (dreamState != null) {
                DreamLazyColumn(
                    dreams = dreamState,
                    onClick = { onEvent(UiEvent.ClickDream(it)) },
                    selectedId = uiState.selectedDream?.id
                )
            }

        }
    }
}