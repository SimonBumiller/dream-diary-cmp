package me.bumiller.ddcmp.domain.stats.person

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.stats.dream.global.base.DreamListBasedStatistic
import me.bumiller.ddcmp.domain.stats.person.PersonOccurrenceStatistic.PersonOccurrenceInfo
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class PersonOccurrenceStatistic(
    allDreams: AllDreamUseCase
): DreamListBasedStatistic<Long, PersonOccurrenceInfo>(allDreams) {

    override suspend fun invoke(
        input: Long,
        dreams: List<AllDreamUseCase.O>,
        actualRange: ClosedRange<LocalDate>
    ): PersonOccurrenceInfo? {
        val dreamsWithPerson = dreams
            .filter { dream ->
                dream.persons.any { input == it.id }
            }

        if(dreamsWithPerson.isEmpty()) return null

        return PersonOccurrenceInfo(
            last = dreamsWithPerson
                .maxOf { it.dream.dreamt },
            first = dreamsWithPerson
                .minOf { it.dream.dreamt }
        )
    }

    data class PersonOccurrenceInfo(
        val first: Instant,
        val last: Instant
    )

    override fun Long.invoke(): ClosedRange<LocalDate> {
        val start = LocalDate(1, 1, 1)
        val end = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        return start..end
    }

}