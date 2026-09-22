package me.ddcmp.feature.home.entries.personlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.home.generated.resources.Res
import dream_diary_cmp.feature.home.generated.resources.personlist_action_add
import dream_diary_cmp.feature.home.generated.resources.personlist_description
import dream_diary_cmp.feature.home.generated.resources.personlist_empty_placeholder
import dream_diary_cmp.feature.home.generated.resources.personlist_title
import me.bumiller.ddcmp.domain.model.Person
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.component.AppInputChip
import me.bumiller.ddcmp.ui.common.component.BackIconButton
import me.bumiller.ddcmp.ui.common.layout.AppBarAction
import me.bumiller.ddcmp.ui.common.layout.AppBarActions
import me.bumiller.ddcmp.ui.common.plus
import me.bumiller.ddcmp.ui.common.style.DisabledContent
import me.ddcmp.feature.home.entries.personlist.PersonListViewModel.UiEvent
import me.ddcmp.feature.home.entries.personlist.PersonListViewModel.UiEvent.AddPerson
import me.ddcmp.feature.home.entries.personlist.PersonListViewModel.UiEvent.GoBack
import me.ddcmp.feature.home.entries.personlist.PersonListViewModel.ViewModelEvent
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PersonListEntry(
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onEdit: (id: Long) -> Unit,
    onGoToPerson: (id: Long) -> Unit
) {
    ViewModelScope<UiEvent, ViewModelEvent, PersonListViewModel>(
        onViewModelEvent = {
            when (it) {
                ViewModelEvent.NavigateBack -> onBack()
                ViewModelEvent.NavigateToAdd -> onAdd()
                is ViewModelEvent.NavigateToEdit -> onEdit(it.id)
                is ViewModelEvent.NavigateToPerson -> onGoToPerson(it.id)
            }
        }
    ) { vm ->
        val persons by vm.persons.collectAsStateWithLifecycle()

        PersonListEntry(vm::onEvent, persons)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersonListEntry(
    onEvent: (UiEvent) -> Unit,
    persons: List<Person>
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(Res.string.personlist_title)) },
                navigationIcon = {
                    BackIconButton { onEvent(GoBack) }
                },
                actions = {
                    AppBarActions(
                        actions = listOf(
                            AppBarAction(
                                label = stringResource(Res.string.personlist_action_add),
                                icon = Icons.Default.Add,
                                handler = { onEvent(AddPerson) }
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
            val msg = if (persons.isEmpty()) Res.string.personlist_empty_placeholder
            else Res.string.personlist_description

            DisabledContent(persons.isEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(msg),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                modifier = Modifier
                    .align(Alignment.End),
                text = "Long-press to edit",
                style = MaterialTheme.typography.labelSmall
            )

            FlowRow(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .weight(1F),
                horizontalArrangement = Arrangement.Center
            ) {
                persons.forEach { person ->
                    AppInputChip(
                        modifier = Modifier
                            .padding(horizontal = 6.dp),
                        label = {
                            Text(person.name)
                        },
                        onClick = { onEvent(UiEvent.GoToPerson(person.id)) },
                        onLongClick = { onEvent(UiEvent.EditPerson(person.id)) },
                        selected = false
                    )
                }
            }
        }
    }
}