package me.bumiller.ddcmp.domain.usecase.tag

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.common.combineSafe
import me.bumiller.ddcmp.domain.infrastructure.DreamRepository
import me.bumiller.ddcmp.domain.infrastructure.PersonRepository
import me.bumiller.ddcmp.domain.infrastructure.TagRepository
import me.bumiller.ddcmp.domain.model.dream.Dream
import me.bumiller.ddcmp.domain.model.dream.Person
import me.bumiller.ddcmp.domain.model.dream.Tag
import me.bumiller.ddcmp.domain.model.meta.SortDirection
import me.bumiller.ddcmp.domain.model.meta.SortMode.TagSort
import me.bumiller.ddcmp.domain.usecase.meta.FlowUseCase
import me.bumiller.ddcmp.domain.usecase.tag.AllTagUseCase.I
import me.bumiller.ddcmp.domain.usecase.tag.AllTagUseCase.O
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
open class AllTagUseCase(
    val repo: TagRepository,
    val personRepo: PersonRepository,
    val dreamRepo: DreamRepository
) : FlowUseCase<I, List<O>> {

    override fun invoke(i: I): Flow<List<O>> = repo.all
        .aggregate()
        .map { it.applyFilter(i) }
        .map { it.applySort(i) }

    private fun List<O>.applySort(i: I) =
        sortedWith { one, two ->
            when (i.sortMode) {
                TagSort.Name -> one.tag.name.compareTo(two.tag.name)
                TagSort.Color -> one.tag.color.compareTo(two.tag.color)
            }
        }.let {
            if (i.sortDirection == SortDirection.Descending) it.reversed()
            else it
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Flow<List<Tag>>.aggregate(): Flow<List<O>> = flatMapLatest { tags ->
        val outputFlows = tags.map { tag ->
            val personFlow = personRepo.getForDream(tag.id)
                .map { it ?: emptyList() }
            val dreamFlow = dreamRepo.getForTag(tag.id)
                .map { it ?: emptyList() }

            combine(
                flow = dreamFlow,
                flow2 = personFlow
            ) { dreams, persons ->
                O(tag, persons, dreams)
            }
        }

        combineSafe(outputFlows) { it.toList() }
    }

    private fun List<O>.applyFilter(i: I) =
        filter { o ->
                    i.personIds?.any { it in o.persons.map(Person::id) } ?: true &&
                    i.dreamIds?.any { it in o.dreams.map(Dream::id) } ?: true &&
                    i.id?.let { o.tag.id == it } ?: true
        }

    data class O(

        val tag: Tag,

        val persons: List<Person>,

        val dreams: List<Dream>

    )

    /**
     * Input type
     */
    data class I(

        /**
         * The sort mode.
         */
        val sortMode: TagSort = TagSort.Color,

        /**
         * The sort direction
         */
        val sortDirection: SortDirection = SortDirection.Ascending,

        /**
         * For which dream-ids to filter.
         */
        val dreamIds: Set<Long>? = null,

        /**
         * For which person-ids to filter.
         */
        val personIds: Set<Long>? = null,

        /**
         * The id of the specific tag to filter for.
         */
        val id: Long? = null
    )

}