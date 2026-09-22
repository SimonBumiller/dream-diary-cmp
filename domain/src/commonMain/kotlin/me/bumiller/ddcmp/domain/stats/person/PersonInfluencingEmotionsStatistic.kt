package me.bumiller.ddcmp.domain.stats.person

import me.bumiller.ddcmp.domain.model.Emotion
import me.bumiller.ddcmp.domain.stats.dream.global.base.DreamConditionalOccurrenceStatistic
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase

/**
 * Statistic that gives insight into how the presence of a person influences the emotions ina  dream.
 */
class PersonInfluencingEmotionsStatistic(
    allDreams: AllDreamUseCase
): DreamConditionalOccurrenceStatistic<Long, Emotion>(allDreams) {

    override fun AllDreamUseCase.O.isRequirementMet(input: Long) = persons.any { it.id == input }

    override fun AllDreamUseCase.O.getAttributes(input: Long): List<Emotion> = emotions
}