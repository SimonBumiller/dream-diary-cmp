package me.bumiller.ddcmp.domain.usecase.dream

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.common.combineSafe
import me.bumiller.ddcmp.domain.infrastructure.DreamRepository
import me.bumiller.ddcmp.domain.infrastructure.EmotionRepository
import me.bumiller.ddcmp.domain.infrastructure.PersonRepository
import me.bumiller.ddcmp.domain.infrastructure.TagRepository
import me.bumiller.ddcmp.domain.model.dream.Dream
import me.bumiller.ddcmp.domain.model.emotion.Emotion
import me.bumiller.ddcmp.domain.model.dream.Person
import me.bumiller.ddcmp.domain.model.dream.Tag
import me.bumiller.ddcmp.domain.model.emotion.Valence
import me.bumiller.ddcmp.domain.model.meta.SortDirection
import me.bumiller.ddcmp.domain.model.meta.SortMode.DreamSort
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase.I
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase.O
import me.bumiller.ddcmp.domain.usecase.meta.FlowUseCase
import kotlin.time.Instant

open class AllDreamUseCase(
    val repo: DreamRepository,
    val emotionRepo: EmotionRepository,
    val personRepo: PersonRepository,
    val tagRepo: TagRepository
) : FlowUseCase<I, List<O>> {

    override fun invoke(i: I): Flow<List<O>> = repo.all
        .aggregate()
        .map { it.applyFilter(i) }
        .map { it.applySort(i) }

    private fun List<O>.applySort(i: I) =
        sortedWith { one, two ->
            when (i.sortMode) {
                DreamSort.AverageValence -> one.emotions.averageValence.compareTo(two.emotions.averageValence)
                DreamSort.Confidentiality -> one.dream.confidentiality.compareTo(two.dream.confidentiality)
                DreamSort.Dreamt -> one.dream.dreamt.compareTo(two.dream.dreamt)
                DreamSort.Name -> one.dream.title.compareTo(two.dream.title)
                DreamSort.Length -> one.dream.length.compareTo(two.dream.length)
            }
        }.let {
            if (i.sortDirection == SortDirection.Descending) it.reversed()
            else it
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Flow<List<Dream>>.aggregate(): Flow<List<O>> = flatMapLatest { dreams ->
        val outputFlows = dreams.map { dream ->
            val emotionFlow = emotionRepo.getForDream(dream.id)
                .map { it ?: emptyList() }
            val personFlow = personRepo.getForDream(dream.id)
                .map { it ?: emptyList() }
            val tagFlow = tagRepo.getForDream(dream.id)
                .map { it ?: emptyList() }

            combine(
                flow = emotionFlow,
                flow2 = personFlow,
                flow3 = tagFlow
            ) { emotions, persons, tags ->
                O(dream, emotions, persons, tags)
            }
        }

        combineSafe(outputFlows) { it.toList() }
    }

    private fun String?.compareTo(other: String?) = if (this == null) {
        if (other == null) 0 else -1
    } else {
        if (other == null) 1 else compareTo(other)
    }

    private fun List<O>.applyFilter(i: I) =
        filter { o ->
            o.dream.dreamt in i.timeRange &&
                    i.emotionIds?.any { it in o.emotions.map(Emotion::id) } ?: true &&
                    i.personIds?.any { it in o.persons.map(Person::id) } ?: true &&
                    i.tagIds?.any { it in o.tags.map(Tag::id) } ?: true &&
                    (!i.onlyFavorite || o.dream.favorite) &&
                    i.id?.let { o.dream.id == it } ?: true
        }

    private val List<Emotion>.averageValence: Float
        get() = map(Emotion::valence).map(Valence::value).average().toFloat()

    data class O(

        val dream: Dream,

        val emotions: List<Emotion>,

        val persons: List<Person>,

        val tags: List<Tag>

    )

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
        val sortDirection: SortDirection = SortDirection.Ascending,

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
         * For which tag-ids to filter.
         */
        val tagIds: Set<Long>? = null,

        /**
         * Whether to only include favorite dreams.
         */
        val onlyFavorite: Boolean = false,

        /**
         * The id of the specific dream to filter for.
         */
        val id: Long? = null
    )

}