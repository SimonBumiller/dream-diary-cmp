package me.bumiller.ddcmp.domain.usecase.person

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.common.combineSafe
import me.bumiller.ddcmp.domain.infrastructure.PersonRepository
import me.bumiller.ddcmp.domain.infrastructure.TagRepository
import me.bumiller.ddcmp.domain.model.dream.Person
import me.bumiller.ddcmp.domain.model.dream.Tag
import me.bumiller.ddcmp.domain.model.meta.SortDirection
import me.bumiller.ddcmp.domain.model.meta.SortMode
import me.bumiller.ddcmp.domain.usecase.meta.FlowUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase.I
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase.O

open class AllPersonUseCase(
    val repo: PersonRepository,
    val tagRepo: TagRepository
) : FlowUseCase<I, List<O>> {

    override fun invoke(i: I): Flow<List<O>> =
        base(i)
            .aggregate()
            .map {
                it.applyFilter(i)
                    .applySort(i)
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Flow<List<Person>>.aggregate(): Flow<List<O>> = flatMapLatest { persons ->
        val outputFlows = persons.map { person ->
            val tagFlow = tagRepo.getForPerson(person.id)
                .map { it ?: emptyList() }

            tagFlow.map { tags ->
                O(person, tags)
            }
        }

        combineSafe(outputFlows) { it.toList() }
    }

    private fun base(i: I) =
        if (i.dreamId == null) repo.all
        else repo.getForDream(i.dreamId)
            .map { it ?: emptyList() }

    private fun List<O>.applySort(i: I) =
        sortedWith { one, two ->
            when (i.sortMode) {
                SortMode.PersonSort.Name -> one.person.name.compareTo(two.person.name)
            }
        }.let {
            if (i.sortDirection == SortDirection.Descending) it.reversed()
            else it
        }

    private fun List<O>.applyFilter(i: I) =
        filter { o ->
            i.id?.let { o.person.id == it } ?: true &&
                    i.tagIds?.any { it in o.tags.map(Tag::id) } ?: true
        }

    /**
     * Data class for the usecase output.
     */
    data class O(

        /**
         * The person
         */
        val person: Person,

        /**
         * The tags for the person.
         */
        val tags: List<Tag>

    )

    /**
     * Input type
     */
    data class I(

        /**
         * The sort mode.
         */
        val sortMode: SortMode.PersonSort = SortMode.PersonSort.Name,

        /**
         * The sort direction
         */
        val sortDirection: SortDirection = SortDirection.Ascending,

        /**
         * The id of the dream of which to get the emotions.
         */
        val dreamId: Long? = null,

        /**
         * The tag-ids to filter for.
         */
        val tagIds: Set<Long>? = null,

        /**
         * The id to search for.
         */
        val id: Long? = null
    )

}