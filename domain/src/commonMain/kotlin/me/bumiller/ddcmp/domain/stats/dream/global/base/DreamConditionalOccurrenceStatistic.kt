package me.bumiller.ddcmp.domain.stats.dream.global.base

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.meta.Unique
import me.bumiller.ddcmp.domain.model.stats.RelativeRelation
import me.bumiller.ddcmp.domain.stats.dream.global.base.DreamConditionalOccurrenceStatistic.ConditionalOccurrenceInfo
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * This statistic calculates how certain dream properties are affected by the presence of another.
 */
@OptIn(ExperimentalTime::class)
abstract class DreamConditionalOccurrenceStatistic<I, Attribute : Unique>(
    allDreams: AllDreamUseCase
) : DreamListBasedStatistic<I, List<ConditionalOccurrenceInfo<Attribute>>>(allDreams) {

    /**
     * Classifies whether a dream meets the requirement.
     *
     * @return The classification
     */
    abstract fun AllDreamUseCase.O.isRequirementMet(input: I): Boolean

    /**
     * Gets all the attributes of the specific dream.
     */
    abstract fun AllDreamUseCase.O.getAttributes(input: I): List<Attribute>

    override suspend fun invoke(
        input: I,
        dreams: List<AllDreamUseCase.O>,
        actualRange: ClosedRange<LocalDate>
    ): List<ConditionalOccurrenceInfo<Attribute>>? {
        // All dreams that meet the requirement
        val dreamsMeetingRequirement = dreams.filter { it.isRequirementMet(input) }

        val meetingRequirementCount = dreamsMeetingRequirement.size
        val allCount = dreams.size

        // If we have only very few dreams, we can't make a proper statistical statement
        if (meetingRequirementCount < 3) return null

        // All attributes that occur at least once with the requirement
        val attributes = dreamsMeetingRequirement
            .flatMap { it.getAttributes(input) }
            .distinctBy { it.id }

        fun AllDreamUseCase.O.hasAttribute(attribute: Attribute) =
            getAttributes(input).any { it.id == attribute.id }

        val infos = attributes.map { attribute ->

            // How often the attribute appears in all dreams
            val totalOccurrences = dreams.count { dream ->
                dream.hasAttribute(attribute)
            }

            // If the attribute occurs only very rarely overall,
            // we can't make a proper statistical observation.
            // Example: Attribute is present only once overall, but once with requirement.
            // Would cause a very high overrepresentation but that has no
            // statistical significance.
            if(totalOccurrences < 3)
                return@map null

            // How often the attribute appears only in the dreams satisfying the requirement
            val occurrencesWithRequirement = dreamsMeetingRequirement.count { dream ->
                dream.hasAttribute(attribute) && dream.isRequirementMet(input)
            }

            // Percentage of how many of the requirement-meeting dreams have the attribute
            val conditionalOccurrence =
                occurrencesWithRequirement.toFloat() / meetingRequirementCount

            // Percentage of how often the attribute is present overall
            val baselineOccurrence = totalOccurrences.toFloat() / allCount
            // The over representation
            val overRepresentation = conditionalOccurrence / baselineOccurrence

            val overRepresentationInterpretation =
                if (overRepresentation >= 1.15F) RelativeRelation.AboveAverage
                else if (overRepresentation >= 0.85) RelativeRelation.Average
                else RelativeRelation.BelowAverage

            ConditionalOccurrenceInfo(
                attribute = attribute,
                count = occurrencesWithRequirement,
                overRepresentation = overRepresentation,
                conditionalOccurrence = conditionalOccurrence,
                overRepresentationClassification = overRepresentationInterpretation
            )
        }

        return infos
            .filterNotNull()
            .sortedByDescending { it.overRepresentation }
    }

    /**
     * Information about how a specific attribute is affected by the requirement.
     */
    data class ConditionalOccurrenceInfo<Attribute>(

        /**
         * The attribute that is influenced by the requirement.
         */
        val attribute: Attribute,

        /**
         * How often the attribute occurs when the requirement is present.
         */
        val count: Int,

        /**
         * How the attribute is effected by the presence of the requirement.
         * > 1: Occurs more when present
         * = 1: Independent
         * < 1: Occurs less when present
         * Calculated by OR = P( Requirement present | Attribute present ) / P( Attribute present )
         */
        val overRepresentation: Float,

        /**
         * Classifies the over representation into an interpreted value.
         */
        val overRepresentationClassification: RelativeRelation,

        /**
         * How often the attribute appears in a dream, given that the requirement is met.
         * Calculated by CO = P( Requirement is met | Attribute is present )
         */
        val conditionalOccurrence: Float
    ) {

        /**
         * The percentage by which the probability is increased for this attribute to be present when the requirement is met.
         * Calculated by 1 - [overRepresentation]
         */
        val percentageIncrease: Float
            get() = overRepresentation - 1

    }

    // We make the calculation over the whole time range.
    override fun I.invoke(): ClosedRange<LocalDate> {
        val start = LocalDate(1, 1, 1)
        val end = Clock.System.now()
            .toLocalDateTime(TimeZone.Companion.currentSystemDefault()).date
        return start..end
    }
}