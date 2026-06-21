package me.bumiller.ddcmp.domain.stats.dream.global

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.model.stats.HistogramMeta
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import kotlin.time.ExperimentalTime

/**
 * A statistical usecase that calculates a histogram (time vs dream frequency) for a specified date range.
 */
@OptIn(ExperimentalTime::class)
class DreamTimeFrequencyHistogram(

    getDreams: AllDreamUseCase

) : DreamListBasedStatistic<ClosedRange<LocalDate>, HistogramMeta<LocalDate, DatePeriod, Int>>(
    getDreams
) {

    override fun ClosedRange<LocalDate>.invoke() = this

    override fun invoke(
        input: ClosedRange<LocalDate>,
        dreams: List<Dream>,
        actualRange: ClosedRange<LocalDate>
    ): HistogramMeta<LocalDate, DatePeriod, Int>? {
        // The size of the buckets that we use to create the frequencies.
        val bucketSize = calculateBucketSize(actualRange)

        // The histogram that maps buckets, identified by the first included day, to the dream counts.
        val histogram = mutableMapOf<LocalDate, Int>()

        var bucketCounter = 0
        var currentBucketStart = actualRange.start
        while (currentBucketStart in actualRange) {
            bucketCounter++
            val currentBucket = currentBucketStart..(currentBucketStart + bucketSize)

            val dreamCountInBucket = dreams.count { dream ->
                val localDateOfDream = dream.dreamt.toLocalDateTime(timeZone).date
                localDateOfDream in currentBucket
            }

            // We put the start of the current bucket and the dream count in the histogram
            histogram[currentBucketStart] = dreamCountInBucket

            // The next bucket starts on the day after the current end
            // We offset by 1 so we don't count the ending several times.
            currentBucketStart = currentBucket.endInclusive + DatePeriod(days = 1)
        }

        if (bucketCounter <= 2) return null

        val meta = HistogramMeta(
            histogram = histogram,
            bucketSize = bucketSize,
            bucketCount = bucketCounter
        )

        return meta
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