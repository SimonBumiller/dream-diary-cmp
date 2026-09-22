package me.bumiller.ddcmp.domain.stats.person

import kotlinx.datetime.LocalDate
import me.bumiller.ddcmp.domain.model.Person
import me.bumiller.ddcmp.domain.stats.dream.global.base.DreamListBasedStatistic
import me.bumiller.ddcmp.domain.stats.person.PersonsOccurrencesCountStatistic.I
import me.bumiller.ddcmp.domain.stats.person.PersonsOccurrencesCountStatistic.PersonWithCount
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase

/**
 * Finds for all persons, how often each person occurred in a dream.
 */
class PersonsOccurrencesCountStatistic(
    allDreams: AllDreamUseCase
) : DreamListBasedStatistic<I, List<PersonWithCount>>(allDreams) {

    override suspend fun invoke(
        input: I,
        dreams: List<AllDreamUseCase.O>,
        actualRange: ClosedRange<LocalDate>
    ): List<PersonWithCount>? {
        val persons = dreams
            .flatMap(AllDreamUseCase.O::persons)
            .distinctBy { it.id }
            .associateWith { 0 }
            .toMutableMap()

        dreams.forEach { o ->
            o.persons.forEach { person ->
                persons[person] = persons.getOrElse(person) { 0 } + 1
            }
        }

        if(persons.size < 3) return null

        return persons
            .map { (person, count) -> PersonWithCount(person, count) }
            .sortedByDescending(PersonWithCount::count)
            .let { persons ->
                if(input.pickTop == null) persons
                else persons.take(input.pickTop)
            }
    }

    override fun I.invoke() = dateRange

    /**
     * Input for the statistic.
     */
    data class I(

        /**
         * The date range.
         */
        val dateRange: ClosedRange<LocalDate>,

        /**
         * How many of the top persons should be picked, or all if null.
         */
        val pickTop: Int? = null

    )

    /**
     * A person together with the count of dreams it is in.
     */
    data class PersonWithCount(

        /**
         * The person.
         */
        val person: Person,

        /**
         * The number of dreams it is in.
         */
        val count: Int
    )

}