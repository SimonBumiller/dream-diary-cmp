package me.bumiller.ddcmp.domain.stats.dream.global

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.stats.dream.global.base.DreamFrequencyHistogramStatistic
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import kotlin.time.ExperimentalTime

/**
 * A statistical usecase that calculates a histogram (day-of-the-week vs dream frequency) for a specified date range.
 */
@OptIn(ExperimentalTime::class)
class DreamWeekdayFrequencyHistogram(
    getDreams: AllDreamUseCase
) : DreamFrequencyHistogramStatistic<ClosedRange<LocalDate>, DayOfWeek, Unit>(getDreams) {

    override fun ClosedRange<LocalDate>.invoke() = this

    override suspend fun createMetadata(
        input: ClosedRange<LocalDate>,
        actualRange: ClosedRange<LocalDate>,
        dreams: List<AllDreamUseCase.O>
    ): Pair<List<DayOfWeek>, Unit> {
        val weekdays = listOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        )

        return weekdays to Unit
    }

    override suspend fun findBucketFor(
        dream: AllDreamUseCase.O,
        buckets: List<DayOfWeek>,
        input: ClosedRange<LocalDate>
    ): DayOfWeek {
        return dream.dream.dreamt.toLocalDateTime(TimeZone.currentSystemDefault()).dayOfWeek
    }

}