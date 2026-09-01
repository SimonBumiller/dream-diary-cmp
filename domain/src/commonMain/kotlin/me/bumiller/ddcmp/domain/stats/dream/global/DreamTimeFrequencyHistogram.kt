package me.bumiller.ddcmp.domain.stats.dream.global

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.stats.dream.global.base.DreamFrequencyHistogramStatistic
import me.bumiller.ddcmp.domain.stats.dream.global.base.DreamHistogramStatistic
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import kotlin.time.ExperimentalTime

/**
 * A statistical usecase that calculates a histogram (time vs dream frequency) for a specified date range.
 */
@OptIn(ExperimentalTime::class)
class DreamTimeFrequencyHistogram(
    getDreams: AllDreamUseCase
) : DreamFrequencyHistogramStatistic<ClosedRange<LocalDate>, LocalDate, DatePeriod>(
    getDreams = getDreams,
    forceEnd = true
) {

    override fun ClosedRange<LocalDate>.invoke() = this

    override suspend fun createMetadata(
        input: ClosedRange<LocalDate>,
        actualRange: ClosedRange<LocalDate>,
        dreams: List<Dream>
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

    override suspend fun findBucketFor(dream: Dream, buckets: List<LocalDate>): LocalDate {
        val dreamDate = dream.dreamt
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

        return if (daysDiff <= 30) DatePeriod(days = 1)
        else if (daysDiff <= 6 * 30) DatePeriod(days = 7)
        else if (daysDiff <= 24 * 30) DatePeriod(days = 21)
        else DatePeriod(months = 1)
    }
}