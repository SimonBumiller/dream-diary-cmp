package me.bumiller.ddcmp.domain.stats.person

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.stats.dream.global.base.DreamFrequencyHistogramStatistic
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * A statistical usecase that calculates a histogram (time vs person frequency) for a specified date range and person.
 */
@OptIn(ExperimentalTime::class)
class PersonTimeFrequencyHistogram(
    getDreams: AllDreamUseCase
) : DreamFrequencyHistogramStatistic<Long, LocalDate, DatePeriod>(
    getDreams = getDreams,
    forceEnd = true
) {

    override fun Long.invoke(): ClosedRange<LocalDate> {
        val start = LocalDate(1, 1, 1)
        val end = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        return start..end
    }

    override suspend fun createMetadata(
        input: Long,
        actualRange: ClosedRange<LocalDate>,
        dreams: List<AllDreamUseCase.O>
    ): Pair<List<LocalDate>, DatePeriod> {
        val bucketSize = calculateBucketSize(actualRange)

        val bucketStarts = mutableListOf<LocalDate>()

        var currentBucketStart = actualRange.start
        while (currentBucketStart in actualRange) {
            bucketStarts += currentBucketStart

            val currentBucketEnd = currentBucketStart + bucketSize
            currentBucketStart = currentBucketEnd + DatePeriod(days = 1)
        }

        return bucketStarts to bucketSize
    }

    override suspend fun findBucketFor(dream: AllDreamUseCase.O, buckets: List<LocalDate>, input: Long): LocalDate? {
        val personInDream = dream.persons.any { it.id == input }

        if(!personInDream) return null

        val dreamDate = dream.dream.dreamt
            .toLocalDateTime(timeZone)
            .date

        return buckets.last { it <= dreamDate }
    }

    /*
     * Calculates the bucket size depending on how long the user has been recording dreams.
     * The longer, the bigger the bucket sizes are.
     */
    private fun calculateBucketSize(range: ClosedRange<LocalDate>): DatePeriod {
        val daysDiff = range.endInclusive.toEpochDays() - range.start.toEpochDays()

        return if (daysDiff <= 30) DatePeriod(days = 2)
        else if (daysDiff <= 6 * 30) DatePeriod(days = 14)
        else if (daysDiff <= 24 * 30) DatePeriod(days = 42)
        else DatePeriod(months = 2)
    }
}