package me.bumiller.ddcmp.domain.stats.person

import me.bumiller.ddcmp.domain.model.dream.Person
import me.bumiller.ddcmp.domain.stats.dream.global.base.DreamConditionalOccurrenceStatistic
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase

/**
 * Statistic that gives insight into how the presence of a person influences the other persons in a dream.
 */
class PersonInfluencingPersonsStatistic(
    allDreams: AllDreamUseCase
): DreamConditionalOccurrenceStatistic<Long, Person>(allDreams) {

    override fun AllDreamUseCase.O.isRequirementMet(input: Long) = persons.any { it.id == input }

    override fun AllDreamUseCase.O.getAttributes(input: Long): List<Person> = persons.filterNot { it.id == input }
}