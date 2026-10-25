package me.ddcmp.feature.home.entries.tagadd

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.bumiller.ddcmp.domain.model.dream.Tag
import me.bumiller.ddcmp.domain.usecase.tag.AllTagUseCase
import me.bumiller.ddcmp.domain.usecase.tag.AllTagUseCase.*
import me.bumiller.ddcmp.domain.usecase.tag.DeleteTagUseCase
import me.bumiller.ddcmp.domain.usecase.tag.SaveTagUseCase
import me.bumiller.ddcmp.domain.usecase.tag.SaveTagUseCase.*
import me.bumiller.ddcmp.domain.util.AppCoroutineScope
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.bumiller.ddcmp.ui.common.component.ColorPickerInitialColor
import me.ddcmp.feature.home.entries.tagadd.TagAddViewModel.UiEvent
import me.ddcmp.feature.home.entries.tagadd.TagAddViewModel.ViewModelEvent
import me.ddcmp.feature.home.entries.tagadd.TagAddViewModel.ViewModelEvent.CloseDialog
import me.ddcmp.feature.home.navigation.HomeRoutes
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class TagAddViewModel(
    private val allTags: AllTagUseCase,
    private val saveTag: SaveTagUseCase,
    savedStateHandle: SavedStateHandle,
    private val appScope: AppCoroutineScope,
    private val deleteTag: DeleteTagUseCase
) : AppViewModel<UiEvent, ViewModelEvent>() {

    private val tagId = savedStateHandle.toRoute<HomeRoutes.AddTag>().tagId

    init {
        registerUiState(TagState())

        if (tagId != null) {
            viewModelScope.launch {
                val tag = if (isAdd) null
                else allTags(AllTagUseCase.I(id = tagId))
                    .map { it.singleOrNull()?.tag }
                    .firstOrNull() ?: return@launch

                updateUiState<TagState> {
                    val newState = it.copy(
                        name = tag?.name ?: "",
                        description = tag?.description ?: "",
                        color = tag?.color ?: ColorPickerInitialColor
                    )
                    initialTagState = newState
                    newState
                }
            }
        }
    }

    override suspend fun handleEvent(event: UiEvent) = when (event) {
        is UiEvent.ChangeName -> updateUiState<TagState> {
            it.copy(name = event.name)
        }

        is UiEvent.ChangeDescription -> updateUiState<TagState> {
            it.copy(description = event.note)
        }

        is UiEvent.ChangeColor -> updateUiState<TagState> {
            it.copy(color = event.color)
        }

        UiEvent.Cancel -> fireEvent(CloseDialog)
        UiEvent.Save -> {
            appScope.launch {
                val existing = if (isAdd) null
                else allTags(AllTagUseCase.I(id = tagId))
                    .map {
                        it.singleOrNull()?.tag
                    }
                    .firstOrNull()

                val state = uiState<TagState>().value

                val now = Clock.System.now()

                val tag = Tag(
                    id = existing?.id ?: 0,
                    created = existing?.created ?: now,
                    edited = now,
                    name = state.name,
                    description = state.description,
                    color = state.color
                )

                SaveTagUseCase.I(
                    tag = tag,
                    persons = emptyList(),
                    dreams = emptyList()
                ).let { saveTag(it) }

                fireEvent(CloseDialog)
            }
            Unit
        }

        UiEvent.ClickDelete -> {
            appScope.launch {
                deleteTag(tagId!!)
            }

            fireEvent(CloseDialog)
        }
    }

    /**
     * State of the person attributes.
     */
    val tagState = uiState<TagState>().asStateFlow()

    /**
     * State that holds whether the content can be saved at the moment.
     */
    val canSave = tagState
        .map { it != initialTagState && it.name.isNotBlank() }
        .stateIn(false)

    /**
     * Whether the view is in add mode.
     */
    val isAdd = tagId == null

    private var initialTagState = TagState()

    data class TagState(
        val name: String = "",
        val description: String = "",
        val color: Long = ColorPickerInitialColor
    )

    sealed interface UiEvent : UIE {
        data class ChangeName(val name: String) : UiEvent
        data class ChangeDescription(val note: String) : UiEvent
        data class ChangeColor(val color: Long) : UiEvent
        data object Cancel : UiEvent
        data object Save : UiEvent
        data object ClickDelete : UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data object CloseDialog : ViewModelEvent
    }

}