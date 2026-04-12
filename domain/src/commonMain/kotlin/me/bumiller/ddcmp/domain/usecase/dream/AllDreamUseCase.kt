package me.bumiller.ddcmp.domain.usecase.dream

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.infrastructure.DreamRepository
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.model.Emotion
import me.bumiller.ddcmp.domain.model.Person
import me.bumiller.ddcmp.domain.model.meta.SortDirection
import me.bumiller.ddcmp.domain.model.meta.SortMode.DreamSort
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase.I
import me.bumiller.ddcmp.domain.usecase.meta.FlowUseCase
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class AllDreamUseCase(
    val repo: DreamRepository
) : FlowUseCase<I, List<Dream>> {

    override suspend fun invoke(i: I): Flow<List<Dream>> = repo.all
        .map { it.applySort(i.sortMode, i.sortDirection).applyFilter(i) }

    private fun List<Dream>.applySort(mode: DreamSort, direction: SortDirection) =
        sortedWith { one, two ->
            when (mode) {
                DreamSort.AverageValence -> one.averageValence.value.compareTo(two.averageValence.value)
                DreamSort.Confidentiality -> one.confidentiality.compareTo(two.confidentiality)
                DreamSort.Dreamt -> one.dreamt.compareTo(two.dreamt)
                DreamSort.Name -> one.title.compareTo(two.title)
            }
        }.let {
            if (direction == SortDirection.Descending) it.reversed()
            else it
        }

    private fun String?.compareTo(other: String?) = if (this == null) {
        if (other == null) 0 else -1
    } else {
        if (other == null) 1 else compareTo(other)
    }

    private fun List<Dream>.applyFilter(i: I) =
        filter { dream ->
            dream.dreamt in i.timeRange &&
                    i.emotionIds?.any { it in dream.emotions.map(Emotion::id) } ?: true &&
                    i.personIds?.any { it in dream.persons.map(Person::id) } ?: true &&
                    (!i.onlyFavorite || dream.favorite)
        }

    /**
     * Input type
     */
    data class I(

        /**
         * The sort mode.
         */
        val sortMode: DreamSort = DreamSort.Dreamt,

        /**
         * The sort direction
         */
        val sortDirection: SortDirection = SortDirection.Descending,

        /**
         * The time range to include in the result.
         * Applies to the dreamt timestamp.
         */
        val timeRange: ClosedRange<Instant> = Instant.DISTANT_PAST..Instant.DISTANT_FUTURE,

        /**
         * For which emotion-ids to filter.
         */
        val emotionIds: Set<Long>? = null,

        /**
         * For which person-ids to filter.
         */
        val personIds: Set<Long>? = null,

        /**
         * Whether to only include favorite dreams.
         */
        val onlyFavorite: Boolean = false
    )

}