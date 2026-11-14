package me.ddcmp.feature.home.entries.taglist

import me.bumiller.ddcmp.domain.model.dream.Tag
import me.bumiller.ddcmp.domain.model.meta.SortMode
import me.bumiller.ddcmp.domain.usecase.tag.AllTagUseCase
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.ddcmp.feature.home.entries.taglist.TagListViewModel.UiEvent
import me.ddcmp.feature.home.entries.taglist.TagListViewModel.ViewModelEvent
import me.ddcmp.feature.home.entries.taglist.TagListViewModel.ViewModelEvent.NavigateAdd
import me.ddcmp.feature.home.entries.taglist.TagListViewModel.ViewModelEvent.NavigateBack

internal class TagListViewModel(
    getTags: AllTagUseCase
): AppViewModel<UiEvent, ViewModelEvent>() {

    private val tagsQueryPersons = AllTagUseCase.I(
        sortMode = SortMode.TagSort.Color,
        target = Tag.TagTarget.Person
    )

    /**
     * The tags to display
     */
    val tagsPerson = getTags(tagsQueryPersons)
        .stateIn(null)

    private val tagsQueryDreams = AllTagUseCase.I(
        sortMode = SortMode.TagSort.Color,
        target = Tag.TagTarget.Dream
    )

    /**
     * The tags to display
     */
    val tagsDream = getTags(tagsQueryDreams)
        .stateIn(null)

    override suspend fun handleEvent(event: UiEvent) = when (event) {
        is UiEvent.ClickEditTag -> fireEvent(ViewModelEvent.NavigateEdit(event.tagId))
        UiEvent.ClickOnAdd -> fireEvent(NavigateAdd)
        UiEvent.ClickOnBack -> fireEvent(NavigateBack)
    }

    sealed interface UiEvent: UIE {
        data object ClickOnAdd: UiEvent
        data object ClickOnBack: UiEvent
        data class ClickEditTag(val tagId: Long): UiEvent
    }

    sealed interface ViewModelEvent: VME {
        data object NavigateBack: ViewModelEvent
        data object NavigateAdd: ViewModelEvent
        data class NavigateEdit(val tagId: Long): ViewModelEvent
    }

}