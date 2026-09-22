package me.bumiller.ddcmp.domain.usecase.emotion

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.infrastructure.EmotionRepository
import me.bumiller.ddcmp.domain.model.Emotion
import me.bumiller.ddcmp.domain.model.EmotionType
import me.bumiller.ddcmp.domain.model.meta.SortDirection
import me.bumiller.ddcmp.domain.model.meta.SortMode
import me.bumiller.ddcmp.domain.usecase.emotion.AllEmotionsUseCase.I
import me.bumiller.ddcmp.domain.usecase.meta.FlowUseCase

class AllEmotionsUseCase(
    val repo: EmotionRepository
) : FlowUseCase<I, List<Emotion>> {

    override fun invoke(i: I): Flow<List<Emotion>> =
        base(i)
            .map { it.applyFilter(i).applySort(i.sortMode, i.sortDirection) }

    private fun List<Emotion>.applyFilter(i: I) =
        filter { emotion ->
            EmotionType.from(emotion) in i.types &&
                    i.id?.let { emotion.id == it } ?: true
        }

    private fun List<Emotion>.applySort(mode: SortMode.EmotionSort, direction: SortDirection) =
        sortedWith { one, two ->
            when (mode) {
                SortMode.EmotionSort.Name -> one.name.compareTo(two.name)
            }
        }.let {
            if(direction == SortDirection.Descending) it.reversed()
            else it
        }

    private fun base(i: I) =
        if (i.dreamId == null) repo.all
        else repo.getForDream(i.dreamId).map { it ?: emptyList() }

    /**
     * Input type
     */
    data class I(

        /**
         * The sort mode.
         */
        val sortMode: SortMode.EmotionSort = SortMode.EmotionSort.Name,

        /**
         * The sort direction
         */
        val sortDirection: SortDirection = SortDirection.Ascending,

        /**
         * The id of the dream of which to get the emotions.
         */
        val dreamId: Long? = null,

        /**
         * The allowed emotion types.
         */
        val types: Set<EmotionType> = EmotionType.entries.toSet(),

        /**
         * The specific id to filter for
         */
        val id: Long? = null
    )

}