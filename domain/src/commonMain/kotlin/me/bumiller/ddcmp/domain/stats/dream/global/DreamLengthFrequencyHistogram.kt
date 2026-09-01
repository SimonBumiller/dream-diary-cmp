package me.bumiller.ddcmp.domain.stats.dream.global

import kotlinx.datetime.LocalDate
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.stats.dream.global.base.DreamFrequencyHistogramStatistic
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import kotlin.time.ExperimentalTime

/**
 * A statistical usecase that calculates a histogram (word count in dream vs dream frequency) for a specified date range.
 */
@OptIn(ExperimentalTime::class)
class DreamLengthFrequencyHistogram(

    getDreams: AllDreamUseCase

) : DreamFrequencyHistogramStatistic<ClosedRange<LocalDate>, Int, Int>(getDreams) {

    override fun ClosedRange<LocalDate>.invoke() = this

    override suspend fun createMetadata(
        input: ClosedRange<LocalDate>,
        actualRange: ClosedRange<LocalDate>,
        dreams: List<Dream>
    ): Pair<List<Int>, Int> {
        val longest = dreams.maxOfOrNull {
            val len = it.length
            println("Got length with description ${it.description}")
            len
        }!!
        val shortest = dreams.minOfOrNull { it.length }!!

        val bucketSize = calculateBucketSize(longest, shortest)

        val bucketStarts = mutableListOf<Int>()

        var currentBucketStart = shortest
        while (currentBucketStart <= longest) {
            bucketStarts += currentBucketStart

            val currentBucketEnd = currentBucketStart + bucketSize
            currentBucketStart = currentBucketEnd + 1
        }

        return bucketStarts to bucketSize
    }

    override suspend fun findBucketFor(dream: Dream, buckets: List<Int>): Int {
        val dreamWordCount = dream.length

        return buckets.last { it <= dreamWordCount }
    }

    private fun calculateBucketSize(longest: Int, shortest: Int): Int {
        val range = longest - shortest

        // The target bucket size.
        return if (range < 10) range / 2
        else if (range < 30) range / 4
        else if (range < 90) range / 8
        else range / 20
    }

}