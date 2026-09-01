package me.bumiller.ddcmp.domain.stats.dream.global.base

import kotlinx.datetime.LocalDate
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.model.stats.Histogram
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase

/**
 * Base for a class that maps each dream to a certain bucket, and then creates a histogram from it.
 */
abstract class DreamHistogramStatistic<I, Bucket, BucketSize, BucketValue : Number>(
    getDreams: AllDreamUseCase,
    forceEnd: Boolean = false
) : DreamListBasedStatistic<I, Histogram<Bucket, BucketSize, BucketValue>>(
    getDreams = getDreams,
    forceEnd = forceEnd
) {

    /**
     * Create the buckets the histogram will consist of.
     */
    protected abstract suspend fun createMetadata(
        input: I,
        actualRange: ClosedRange<LocalDate>,
        dreams: List<Dream>
    ): Pair<List<Bucket>, BucketSize>?

    /**
     * Finds the bucket for a specific dream.
     *
     * @param dream The dream for which the bucket should be determined.
     * @return The bucket of the dream
     */
    protected abstract suspend fun findBucketFor(dream: Dream, buckets: List<Bucket>): Bucket

    /**
     * Finds the value of the dream.
     *
     * @param dream The dream
     * @return The value of the dream.
     */
    protected abstract fun getDreamValue(dream: Dream, bucket: Bucket): BucketValue

    /**
     * Combine several bucket values into one.
     *
     * @param values The bucket values
     * @return The aggregated value
     */
    protected abstract fun combineValues(values: List<BucketValue>): BucketValue

    override suspend fun invoke(
        input: I,
        dreams: List<Dream>,
        actualRange: ClosedRange<LocalDate>
    ): Histogram<Bucket, BucketSize, BucketValue>? {
        val (buckets, bucketSize) = createMetadata(input, actualRange, dreams) ?: return null

        val dreamsWithBuckets = dreams.associateWith {
            findBucketFor(it, buckets)
        }
            .apply {
                values.forEach { bucket ->
                    require(bucket in buckets) { "Dream was assigned a bucket that it does not belong to." }
                }
            }

        val bucketsWithDreams = dreamsWithBuckets.entries
            .groupBy(
                keySelector = { it.value },
                valueTransform = { it.key }
            )

        val bucketsWithValues = bucketsWithDreams.map { (bucket, dreams) ->
            val values = dreams.map { getDreamValue(it, bucket) }
            val value = combineValues(values)

            bucket to value
        }.toMap()

        return Histogram(
            dataMap = bucketsWithValues,
            bucketSize = bucketSize
        )
    }

}