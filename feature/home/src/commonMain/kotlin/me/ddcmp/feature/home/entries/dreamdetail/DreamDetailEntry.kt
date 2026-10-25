package me.ddcmp.feature.home.entries.dreamdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LineAxis
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dream_diary_cmp.feature.home.generated.resources.Res
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_action_delete
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_action_edit
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_action_favorite
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_action_stats
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_confidentiality_post
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_confidentiality_pre
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_downloaded_snackbar
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_downloaded_snackbar_action
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_emotions_info
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_emotions_info_empty
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_emotions_title
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_note_label
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_persons_info
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_persons_info_empty
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_persons_title
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_tags_info
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_tags_info_empty
import dream_diary_cmp.feature.home.generated.resources.dreamdetail_tags_title
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.compose.SaverResultLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import me.bumiller.ddcmp.domain.model.attachment.AttachmentMeta
import me.bumiller.ddcmp.domain.model.dream.Confidentiality
import me.bumiller.ddcmp.domain.model.dream.Dream
import me.bumiller.ddcmp.domain.model.dream.Person
import me.bumiller.ddcmp.domain.model.dream.Tag
import me.bumiller.ddcmp.domain.model.emotion.Emotion
import me.bumiller.ddcmp.ui.common.ViewModelScope
import me.bumiller.ddcmp.ui.common.component.AppTagInputChip
import me.bumiller.ddcmp.ui.common.component.BackIconButton
import me.bumiller.ddcmp.ui.common.component.MultiStyleText
import me.bumiller.ddcmp.ui.common.component.TextStyleWithCallback
import me.bumiller.ddcmp.ui.common.format.formatInstant
import me.bumiller.ddcmp.ui.common.layout.AppBarAction
import me.bumiller.ddcmp.ui.common.layout.AppBarActions
import me.bumiller.ddcmp.ui.common.plus
import me.bumiller.ddcmp.ui.common.viewDirectory
import me.bumiller.ddcmp.ui.common.viewFile
import me.ddcmp.feature.home.component.AttachmentItem
import me.ddcmp.feature.home.component.UiAttachment
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailViewModel.UiEvent
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailViewModel.ViewModelEvent
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DreamDetailEntry(
    onEdit: (id: Long) -> Unit,
    onGoToEmotion: (id: Long) -> Unit,
    onGoToPerson: (id: Long) -> Unit,
    onGoToDreamStats: (id: Long) -> Unit,
    onBack: () -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }

    val snackbarMessage = stringResource(Res.string.dreamdetail_downloaded_snackbar)
    val snackbarAction = stringResource(Res.string.dreamdetail_downloaded_snackbar_action)

    var saverLauncher: SaverResultLauncher? = null

    ViewModelScope<UiEvent, ViewModelEvent, DreamDetailViewModel>(
        onViewModelEvent = {
            when (it) {
                is ViewModelEvent.GoToEdit -> onEdit(it.dreamId)
                is ViewModelEvent.GoToEmotion -> onGoToEmotion(it.emotionId)
                is ViewModelEvent.GoToPerson -> onGoToPerson(it.personId)
                ViewModelEvent.GoBack -> onBack()
                is ViewModelEvent.GoToDreamStats -> onGoToDreamStats(it.dreamId)
                is ViewModelEvent.OpenSaverPicker -> {
                    saverLauncher?.launch(it.filename)
                }

                is ViewModelEvent.ShowFile -> viewFile(it.file, it.attachment.mimeType)
                ViewModelEvent.DownloadFinished -> {
                    val result = snackbarState.showSnackbar(
                        message = snackbarMessage,
                        actionLabel = snackbarAction
                    )
                    if(result == SnackbarResult.ActionPerformed)
                        onEvent(UiEvent.GoToRecentlyDownloadedAttachment)
                }

                is ViewModelEvent.OpenDirectory -> viewDirectory(it.directory)
            }
        }
    ) { vm ->
        saverLauncher = rememberFileSaverLauncher(
            dialogSettings = FileKitDialogSettings.createDefault()
        ) {
            vm.onEvent(UiEvent.SelectDownloadDestination(it))
        }

        val state by vm.dreamState.collectAsStateWithLifecycle()

        if (state == null)
            return@ViewModelScope

        if (state != null) {
            val (dream, emotions, persons, tags, attachments) = state!!

            DreamDetailEntry(
                vm::onEvent,
                snackbarState,
                dream,
                emotions,
                tags,
                persons,
                attachments
            )
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
private fun DreamDetailEntry(
    onEvent: (UiEvent) -> Unit,
    snackbarState: SnackbarHostState,
    dream: Dream,
    emotions: List<Emotion>,
    tags: List<Tag>,
    persons: List<Person>,
    attachments: List<AttachmentMeta>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(formatInstant(dream.dreamt))
                },
                navigationIcon = {
                    BackIconButton { onEvent(UiEvent.Back) }
                },
                actions = {
                    AppBarActions(
                        actions = listOf(
                            AppBarAction(
                                label = stringResource(Res.string.dreamdetail_action_favorite),
                                icon = if (dream.favorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                handler = { onEvent(UiEvent.ToggleFavorite) }
                            ),
                            AppBarAction(
                                label = stringResource(Res.string.dreamdetail_action_edit),
                                icon = Icons.Default.Edit,
                                handler = { onEvent(UiEvent.Edit) }
                            ),
                            AppBarAction(
                                label = stringResource(Res.string.dreamdetail_action_stats),
                                icon = Icons.Default.LineAxis,
                                handler = { onEvent(UiEvent.GoToDreamStats) }
                            ),
                            AppBarAction(
                                label = stringResource(Res.string.dreamdetail_action_delete),
                                icon = Icons.Default.Delete,
                                handler = { onEvent(UiEvent.ClickDelete) }
                            )
                        )
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarState)
        }
    ) { pv ->
        LazyColumn(
            modifier = Modifier
                .padding(pv + PaddingValues(horizontal = 8.dp))
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(
                key = "header"
            ) {
                Column {
                    if (dream.title != null) {
                        Text(
                            text = dream.title!!,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = dream.description,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify
                    )
                }
            }

            items(
                count = attachments.size,
                key = { attachments[it].fileSize } // Size is approximately unique enough
            ) { index ->
                val attachment = attachments[index].let {
                    object : UiAttachment {
                        override val name = it.name
                        override val mimeType = it.mimeType
                        override val size = it.fileSize
                        override val fileName = it.originalFilename
                    }
                }

                AttachmentItem(
                    modifier = Modifier
                        .widthIn(min = 350.dp)
                        .padding(horizontal = 8.dp)
                        .animateItem(),
                    meta = attachment,
                    onClick = { onEvent(UiEvent.ClickAttachment(attachments[index])) },
                    onDownload = { onEvent(UiEvent.ClickDownloadAttachment(attachments[index])) }
                )
            }

            item(
                key = "card"
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
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
                            text = stringResource(Res.string.dreamdetail_persons_title),
                            style = MaterialTheme.typography.titleMediumEmphasized
                        )

                        Text(
                            text = if (persons.isNotEmpty()) pluralStringResource(
                                Res.plurals.dreamdetail_persons_info,
                                persons.size,
                                persons.size
                            )
                            else stringResource(Res.string.dreamdetail_persons_info_empty),
                            style = MaterialTheme.typography.bodySmall
                        )

                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            persons.forEach { person ->
                                InputChip(
                                    selected = false,
                                    onClick = { onEvent(UiEvent.ClickPerson(person)) },
                                    label = { Text(person.name) }
                                )
                            }
                        }
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
                            text = stringResource(Res.string.dreamdetail_tags_title),
                            style = MaterialTheme.typography.titleMediumEmphasized
                        )

                        Text(
                            text = if (tags.isNotEmpty()) pluralStringResource(
                                Res.plurals.dreamdetail_tags_info,
                                tags.size,
                                tags.size
                            )
                            else stringResource(Res.string.dreamdetail_tags_info_empty),
                            style = MaterialTheme.typography.bodySmall
                        )

                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            tags.forEach { tag ->
                                AppTagInputChip(
                                    selected = false,
                                    tag = tag,
                                    onClick = { }
                                )
                            }
                        }
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
                            text = stringResource(Res.string.dreamdetail_emotions_title),
                            style = MaterialTheme.typography.titleMediumEmphasized
                        )

                        Text(
                            text = if (emotions.isNotEmpty()) pluralStringResource(
                                Res.plurals.dreamdetail_emotions_info,
                                emotions.size,
                                emotions.size
                            )
                            else stringResource(Res.string.dreamdetail_emotions_info_empty),
                            style = MaterialTheme.typography.bodySmall
                        )

                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            emotions.forEach { emotion ->
                                InputChip(
                                    selected = false,
                                    onClick = { onEvent(UiEvent.ClickEmotion(emotion)) },
                                    label = { Text(emotion.name) }
                                )
                            }
                        }
                    }

                    dream.note?.let { note ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 8.dp,
                                    end = 8.dp,
                                    top = 4.dp,
                                    bottom = 12.dp
                                )
                        ) {
                            Text(
                                text = stringResource(Res.string.dreamdetail_note_label),
                                style = MaterialTheme.typography.titleMediumEmphasized
                            )

                            Text(
                                text = note,
                                style = MaterialTheme.typography.bodySmall,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }

                }
            }

            if (dream.confidentiality != Confidentiality.None) {
                item {
                    MultiStyleText(
                        modifier = Modifier.fillMaxWidth(),
                        defaultStyle = MaterialTheme.typography.bodyMedium,
                        stringResource(Res.string.dreamdetail_confidentiality_pre) to null,
                        dream.confidentiality.name to TextStyleWithCallback(
                            MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        ),
                        stringResource(Res.string.dreamdetail_confidentiality_post) to null
                    )
                }
            }
        }
    }
}