package me.ddcmp.feature.home.entries.dreamadd

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.home.generated.resources.Res
import dream_diary_cmp.feature.home.generated.resources.dream_title
import dream_diary_cmp.feature.home.generated.resources.dreamadd_actions_add_attachment
import dream_diary_cmp.feature.home.generated.resources.dreamadd_actions_save
import dream_diary_cmp.feature.home.generated.resources.dreamadd_confidentiality_info
import dream_diary_cmp.feature.home.generated.resources.dreamadd_confidentiality_title
import dream_diary_cmp.feature.home.generated.resources.dreamadd_description_placeholder
import dream_diary_cmp.feature.home.generated.resources.dreamadd_emotions_info
import dream_diary_cmp.feature.home.generated.resources.dreamadd_emotions_label
import dream_diary_cmp.feature.home.generated.resources.dreamadd_emotions_title
import dream_diary_cmp.feature.home.generated.resources.dreamadd_note_info
import dream_diary_cmp.feature.home.generated.resources.dreamadd_note_label
import dream_diary_cmp.feature.home.generated.resources.dreamadd_persons_info
import dream_diary_cmp.feature.home.generated.resources.dreamadd_persons_label
import dream_diary_cmp.feature.home.generated.resources.dreamadd_persons_title
import dream_diary_cmp.feature.home.generated.resources.dreamadd_tags_info
import dream_diary_cmp.feature.home.generated.resources.dreamadd_tags_label
import dream_diary_cmp.feature.home.generated.resources.dreamadd_tags_title
import dream_diary_cmp.feature.home.generated.resources.dreamadd_title
import dream_diary_cmp.feature.home.generated.resources.dreamadd_title_info
import dream_diary_cmp.feature.home.generated.resources.dreamadd_title_label
import io.github.vinceglb.filekit.dialogs.compose.PickerResultLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import kotlinx.coroutines.delay
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.dream.Person
import me.bumiller.ddcmp.domain.model.dream.Tag
import me.bumiller.ddcmp.domain.model.emotion.Emotion
import me.bumiller.ddcmp.domain.util.search
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.backhandler.PlatformBackHandler
import me.bumiller.ddcmp.ui.common.component.AppTagInputChip
import me.bumiller.ddcmp.ui.common.component.BackIconButton
import me.bumiller.ddcmp.ui.common.component.ConfidentialitySelector
import me.bumiller.ddcmp.ui.common.component.SearchTextField
import me.bumiller.ddcmp.ui.common.dialog.DDCDatePickerDialog
import me.bumiller.ddcmp.ui.common.dialog.DiscardChangesDialog
import me.bumiller.ddcmp.ui.common.format.formatInstant
import me.bumiller.ddcmp.ui.common.format.formatLocalDate
import me.bumiller.ddcmp.ui.common.layout.AppBarAction
import me.bumiller.ddcmp.ui.common.layout.AppBarActions
import me.bumiller.ddcmp.ui.common.layout.ColumnOrRow
import me.bumiller.ddcmp.ui.common.layout.TopBarWithProgressIndicator
import me.bumiller.ddcmp.ui.common.viewFile
import me.ddcmp.feature.home.component.AttachmentItem
import me.ddcmp.feature.home.entries.dreamadd.DreamAddViewModel.UiEvent
import me.ddcmp.feature.home.entries.dreamadd.DreamAddViewModel.UiEvent.ChangeDescription
import me.ddcmp.feature.home.entries.dreamadd.DreamAddViewModel.UiEvent.SelectPerson
import me.ddcmp.feature.home.entries.dreamadd.component.AddAttachmentDialog
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun DreamAddEntry(
    onBack: () -> Unit
) {
    var attachmentPickerLauncher: PickerResultLauncher? = null

    ViewModelScope<UiEvent, DreamAddViewModel.ViewModelEvent, DreamAddViewModel>(
        onViewModelEvent = {
            when (it) {
                DreamAddViewModel.ViewModelEvent.GoBack -> onBack()
                DreamAddViewModel.ViewModelEvent.LaunchAttachmentPicker -> attachmentPickerLauncher?.launch()
                is DreamAddViewModel.ViewModelEvent.ShowAttachment -> {
                    viewFile(it.file, it.attachment.mimeType)
                }
            }
        }
    ) { vm ->
        val uiState by vm.uiState.collectAsStateWithLifecycle()
        val dreamState by vm.dreamState.collectAsStateWithLifecycle()
        val canSave by vm.canSave.collectAsStateWithLifecycle()
        val persons by vm.personsState.collectAsStateWithLifecycle()
        val tags by vm.tagState.collectAsStateWithLifecycle()
        val emotions by vm.emotionsState.collectAsStateWithLifecycle()

        attachmentPickerLauncher = rememberFilePickerLauncher {
            it?.let { vm.onEvent(UiEvent.SelectAttachmentFile(it)) }
        }

        uiState.unfinishedAttachment?.let { unfinishedFile ->
            AddAttachmentDialog(
                file = unfinishedFile,
                onSave = { draft ->
                    vm.onEvent(UiEvent.SelectAttachmentFile(null))
                    if (draft != null) {
                        vm.onEvent(UiEvent.AddAttachmentDraft(draft))
                    }
                }
            )
        }

        DreamAddEntry(vm::onEvent, uiState, dreamState, canSave, persons, tags, emotions)
    }
}

val transparentTextFieldColors: TextFieldColors
    @Composable get() = TextFieldDefaults.colors().copy(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        focusedContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent
    )

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
private fun DreamAddEntry(
    onEvent: (UiEvent) -> Unit,
    uiState: DreamAddViewModel.UiState,
    dreamState: DreamAddViewModel.DreamState,
    canSave: Boolean,
    persons: List<Person>,
    tags: List<Tag>,
    emotions: List<Emotion>
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(300L.milliseconds)
        if (!uiState.isEdit) {
            keyboardController?.run {
                show()
                focusRequester.requestFocus()
            }
        }
    }

    PlatformBackHandler {
        onEvent(UiEvent.NavigateBack)
    }

    if (uiState.showDiscardDialog) {
        DiscardChangesDialog(
            onDiscard = { onEvent(UiEvent.NavigateBack) },
            onKeep = { onEvent(UiEvent.HideDiscardDialog) }
        )
    }

    if (uiState.showDreamtDialog) {
        DDCDatePickerDialog(
            onDismiss = { onEvent(UiEvent.ToggleDreamtDialog) },
            onSelect = { _, instant ->
                onEvent(UiEvent.ChangeDreamtDate(instant))
                onEvent(UiEvent.ToggleDreamtDialog)
            },
            initialSelected = dreamState.dreamt
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBarWithProgressIndicator(
                loading = !uiState.finishedLoading
            ) {
                TopAppBar(
                    title = { Text(stringResource(Res.string.dreamadd_title)) },
                    subtitle = {
                        if (uiState.finishedLoading) {
                            Text(
                                modifier = Modifier
                                    .clickable(
                                        onClick = { onEvent(UiEvent.ToggleDreamtDialog) }
                                    ),
                                text = formatLocalDate(dreamState.dreamt.toLocalDateTime(TimeZone.currentSystemDefault()).date)
                            )
                        }
                    },
                    actions = {
                        AppBarActions(
                            actions = listOf(
                                AppBarAction(
                                    label = stringResource(Res.string.dreamadd_actions_add_attachment),
                                    icon = Icons.Default.AttachFile,
                                    handler = { onEvent(UiEvent.ClickAddAttachment) }
                                ),
                                AppBarAction(
                                    label = stringResource(Res.string.dreamadd_actions_save),
                                    icon = Icons.Default.Save,
                                    handler = { onEvent(UiEvent.Save) },
                                    enabled = canSave
                                ),
                            )
                        )
                    },
                    navigationIcon = {
                        BackIconButton { onEvent(UiEvent.NavigateBack) }
                    }
                )
            }
        }
    ) { pv ->
        if (!uiState.finishedLoading) return@Scaffold

        LazyColumn(
            modifier = Modifier
                .padding(pv)
                .fillMaxSize()
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(
                key = "oisdoijfjoi44g"
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = dreamState.description,
                    onValueChange = { onEvent(ChangeDescription(it)) },
                    placeholder = { Text(stringResource(Res.string.dreamadd_description_placeholder)) },
                    keyboardOptions = KeyboardOptions().copy(
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrectEnabled = true

                    ),
                    minLines = 4,
                    colors = transparentTextFieldColors
                )
            }

            items(
                count = dreamState.attachments.size,
                key = { dreamState.attachments[it].size } // Size is approximately unique enough
            ) {
                val attachment = dreamState.attachments[it]

                AttachmentItem(
                    modifier = Modifier
                        .widthIn(min = 350.dp)
                        .animateItem(),
                    meta = attachment,
                    onClick = { onEvent(UiEvent.ClickAttachment(attachment)) },
                    onRemove = { onEvent(UiEvent.RemoveAttachment(attachment)) },
                    isNew = attachment is DreamAddViewModel.UiAttachment.Draft
                )
            }

            item(
                key = "oifjgodgijdofigj"
            ) {
                ColumnOrRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(),
                    doColumn = { it.widthSizeClass != WindowWidthSizeClass.Expanded },
                    first = {
                        MainMetaSection(dreamState, onEvent, uiState, persons, tags, emotions)
                    },
                    second = {
                        OptionalSection(dreamState, onEvent)
                    },
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                )
            }
        }
    }
}

@Composable
private fun OptionalSection(
    dreamState: DreamAddViewModel.DreamState,
    onEvent: (UiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(Res.string.dreamadd_title_info),
            style = MaterialTheme.typography.bodySmall
        )

        OutlinedTextField(
            value = dreamState.title,
            onValueChange = { onEvent(UiEvent.ChangeTitle(it)) },
            placeholder = {
                Text(
                    stringResource(
                        Res.string.dream_title,
                        formatInstant(Clock.System.now())
                    )
                )
            },
            label = { Text(stringResource(Res.string.dreamadd_title_label)) }
        )

        Spacer(
            modifier = Modifier
                .height(8.dp)
        )

        Text(
            text = stringResource(Res.string.dreamadd_note_info),
            style = MaterialTheme.typography.bodySmall
        )

        OutlinedTextField(
            value = dreamState.note,
            onValueChange = { onEvent(UiEvent.ChangeNote(it)) },
            label = { Text(stringResource(Res.string.dreamadd_note_label)) },
            minLines = 3
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun MainMetaSection(
    dreamState: DreamAddViewModel.DreamState,
    onEvent: (UiEvent) -> Unit,
    uiState: DreamAddViewModel.UiState,
    persons: List<Person>,
    tags: List<Tag>,
    emotions: List<Emotion>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 4.dp,
                    bottom = 12.dp
                ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(Res.string.dreamadd_persons_title),
                style = MaterialTheme.typography.titleMediumEmphasized
            )

            Text(
                text = stringResource(Res.string.dreamadd_persons_info),
                style = MaterialTheme.typography.bodySmall
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dreamState.persons.forEach { person ->
                    InputChip(
                        selected = false,
                        onClick = { onEvent(UiEvent.UnSelectPerson(person)) },
                        label = { Text(person.name) }
                    )
                }
            }

            SearchTextField(
                query = uiState.personQuery,
                onQueryChange = { onEvent(UiEvent.ChangePersonQuery(it)) },
                label = { Text(stringResource(Res.string.dreamadd_persons_label)) },
                values = persons.search(uiState.personQuery),
                name = Person::name,
                onSelect = { onEvent(SelectPerson(it)) }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 4.dp,
                    bottom = 12.dp
                ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(Res.string.dreamadd_tags_title),
                style = MaterialTheme.typography.titleMediumEmphasized
            )

            Text(
                text = stringResource(Res.string.dreamadd_tags_info),
                style = MaterialTheme.typography.bodySmall
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dreamState.tags.forEach { tag ->
                    AppTagInputChip(
                        selected = false,
                        tag = tag,
                        onClick = { onEvent(UiEvent.UnselectTag(tag)) }
                    )
                }
            }

            SearchTextField(
                query = uiState.tagQuery,
                onQueryChange = { onEvent(UiEvent.ChangeTagQuery(it)) },
                label = { Text(stringResource(Res.string.dreamadd_tags_label)) },
                values = tags.search(uiState.tagQuery),
                name = Tag::name,
                onSelect = { onEvent(UiEvent.SelectTag(it)) }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 4.dp,
                    bottom = 12.dp
                ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(Res.string.dreamadd_emotions_title),
                style = MaterialTheme.typography.titleMediumEmphasized
            )

            Text(
                text = stringResource(Res.string.dreamadd_emotions_info),
                style = MaterialTheme.typography.bodySmall
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dreamState.emotions.forEach { emotion ->
                    InputChip(
                        selected = false,
                        onClick = { onEvent(UiEvent.UnSelectEmotion(emotion)) },
                        label = { Text(emotion.name) }
                    )
                }
            }

            SearchTextField(
                query = uiState.emotionQuery,
                onQueryChange = { onEvent(UiEvent.ChangeEmotionQuery(it)) },
                label = { Text(stringResource(Res.string.dreamadd_emotions_label)) },
                values = emotions.search(uiState.emotionQuery),
                name = Emotion::name,
                onSelect = { onEvent(UiEvent.SelectEmotion(it)) }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 4.dp,
                    bottom = 12.dp
                ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(Res.string.dreamadd_confidentiality_title),
                style = MaterialTheme.typography.titleMediumEmphasized
            )

            Text(
                text = stringResource(Res.string.dreamadd_confidentiality_info),
                style = MaterialTheme.typography.bodySmall
            )

            ConfidentialitySelector(
                modifier = Modifier
                    .fillMaxWidth(),
                confidentiality = dreamState.confidentiality,
                onSelect = { onEvent(UiEvent.SetConfidentiality(it)) }
            )
        }
    }
}