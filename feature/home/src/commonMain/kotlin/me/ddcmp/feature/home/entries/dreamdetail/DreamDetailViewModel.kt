package me.ddcmp.feature.home.entries.dreamdetail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.bumiller.ddcmp.domain.model.attachment.AttachmentMeta
import me.bumiller.ddcmp.domain.model.dream.Person
import me.bumiller.ddcmp.domain.model.emotion.Emotion
import me.bumiller.ddcmp.domain.usecase.attachment.DownloadAttachmentUseCase
import me.bumiller.ddcmp.domain.usecase.attachment.GetAttachmentsUseCase
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase.I
import me.bumiller.ddcmp.domain.util.AppCoroutineScope
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailViewModel.UiEvent
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailViewModel.ViewModelEvent
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailViewModel.ViewModelEvent.DownloadFinished
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailViewModel.ViewModelEvent.GoBack
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailViewModel.ViewModelEvent.GoToDreamStats
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailViewModel.ViewModelEvent.GoToEdit
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailViewModel.ViewModelEvent.GoToEmotion
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailViewModel.ViewModelEvent.GoToPerson
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailViewModel.ViewModelEvent.OpenSaverPicker
import me.ddcmp.feature.home.entries.dreamdetail.DreamDetailViewModel.ViewModelEvent.ShowFile
import me.ddcmp.feature.home.navigation.HomeRoutes

internal class DreamDetailViewModel(
    allDreams: AllDreamUseCase,
    savedStateHandle: SavedStateHandle,
    private val saveDream: SaveDreamUseCase,
    private val appScope: AppCoroutineScope,
    private val getAttachment: GetAttachmentsUseCase,
    private val download: DownloadAttachmentUseCase
) : AppViewModel<UiEvent, ViewModelEvent>() {

    private val navRoute = savedStateHandle.toRoute<HomeRoutes.DreamDetail>()

    /**
     * The state containing the dream to display.
     */
    val dreamState = allDreams(AllDreamUseCase.I(id = navRoute.dreamId))
        .map { it.singleOrNull() }
        .stateIn(null)

    /**
     * The attachment that is currently downloading (or the path being chosen for)
     */
    var currentlyDownloadingAttachment: AttachmentMeta? = null

    /**
     * The local file of the attachment that was recently downloaded
     */
    var recentlyDownloadedAttachment: PlatformFile? = null

    override suspend fun handleEvent(event: UiEvent) = when (event) {
        UiEvent.GoToDreamStats -> fireEvent(GoToDreamStats(navRoute.dreamId))
        UiEvent.ToggleFavorite -> appScope.launch {
            val state = dreamState.value
            val newDream = state?.dream?.let {
                it.copy(favorite = !it.favorite)
            }

            if (state != null) {
                val i = I(
                    dream = newDream!!,
                    persons = state.persons,
                    emotions = state.emotions
                )
                saveDream(i)
            }
        }.let { Unit }

        UiEvent.Edit -> fireEvent(GoToEdit(navRoute.dreamId))
        is UiEvent.ClickEmotion -> fireEvent(GoToEmotion(event.emotion.id))
        is UiEvent.ClickPerson -> fireEvent(GoToPerson(event.person.id))
        UiEvent.Back -> fireEvent(GoBack)
        is UiEvent.ClickAttachment -> {
            val query = GetAttachmentsUseCase.I(event.attachment.id)
            val file = getAttachment(query).first().singleOrNull()?.file
                ?: error("Didn't find attachment file.")
            fireEvent(ShowFile(file, event.attachment))
        }

        is UiEvent.ClickDownloadAttachment -> {
            fireEvent(OpenSaverPicker(event.attachment.originalFilename))
            currentlyDownloadingAttachment = event.attachment
        }

        is UiEvent.SelectDownloadDestination -> {
            val attachment = currentlyDownloadingAttachment
            currentlyDownloadingAttachment = null

            if (event.destination != null && attachment != null) {
                val downloadConfig = DownloadAttachmentUseCase.I(attachment.id, event.destination)

                if (download(downloadConfig)) {
                    recentlyDownloadedAttachment = event.destination
                    fireEvent(DownloadFinished)
                }
            }

            Unit
        }

        UiEvent.GoToRecentlyDownloadedAttachment -> fireEvent(
            ViewModelEvent.OpenDirectory(
                recentlyDownloadedAttachment!!
            )
        )
    }

    sealed interface UiEvent : UIE {
        data object GoToDreamStats : UiEvent
        data object ToggleFavorite : UiEvent
        data object Edit : UiEvent
        data object Back : UiEvent
        data class ClickEmotion(val emotion: Emotion) : UiEvent
        data class ClickPerson(val person: Person) : UiEvent
        data class ClickAttachment(val attachment: AttachmentMeta) : UiEvent
        data class ClickDownloadAttachment(val attachment: AttachmentMeta) : UiEvent
        data class SelectDownloadDestination(val destination: PlatformFile?) : UiEvent
        data object GoToRecentlyDownloadedAttachment : UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data class GoToEdit(val dreamId: Long) : ViewModelEvent
        data class GoToDreamStats(val dreamId: Long) : ViewModelEvent
        data class GoToEmotion(val emotionId: Long) : ViewModelEvent
        data class GoToPerson(val personId: Long) : ViewModelEvent
        data class ShowFile(val file: PlatformFile, val attachment: AttachmentMeta) : ViewModelEvent
        data object GoBack : ViewModelEvent
        data class OpenSaverPicker(val filename: String) : ViewModelEvent
        data class OpenDirectory(val directory: PlatformFile) : ViewModelEvent
        data object DownloadFinished : ViewModelEvent
    }

}