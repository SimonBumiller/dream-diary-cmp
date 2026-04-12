package me.bumiller.ddcmp.domain.usecase.person

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.infrastructure.DreamRepository
import me.bumiller.ddcmp.domain.infrastructure.PersonRepository
import me.bumiller.ddcmp.domain.model.Person
import me.bumiller.ddcmp.domain.model.meta.SortDirection
import me.bumiller.ddcmp.domain.model.meta.SortMode
import me.bumiller.ddcmp.domain.usecase.meta.FlowUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase.I

class AllPersonUseCase(
    val repo: PersonRepository,
    val dreamRepo: DreamRepository
) : FlowUseCase<I, List<Person>> {

    override fun invoke(i: I): Flow<List<Person>> =
        base(i)
            .map { it.applySort(i.sortMode, i.sortDirection) }

    private fun base(i: I) =
        if (i.dreamId == null) repo.all
        else dreamRepo.id(i.dreamId)
            .map { it?.persons ?: emptyList() }

    private fun List<Person>.applySort(mode: SortMode.PersonSort, direction: SortDirection) =
        sortedWith { one, two ->
            when (mode) {
                SortMode.PersonSort.Name -> one.name.compareTo(two.name)
            }
        }.let {
            if (direction == SortDirection.Descending) it.reversed()
            else it
        }

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
        val sortDirection: SortDirection = SortDirection.Descending,

        /**
         * The id of the dream of which to get the emotions.
         */
        val dreamId: Long? = null
    )

}