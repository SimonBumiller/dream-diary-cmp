package me.bumiller.ddcmp.domain.model.stats

/**
 * Meta-information about a numerical histogram.
 */
data class HistogramMeta<Bucket, BucketSize, Value : Number>(

    /**
     * The actual histogram.
     */
    val histogram: Map<Bucket, Value>,

    /**
     * The size of a single bucket.
     */
    val bucketSize: BucketSize,

    /**
     * The number of buckets
     */
    val bucketCount: Int

) {

    /**
     * The average number per bucket.
     */
    val average: Float
        get() = histogram.values.map { it.toFloat() }.sum() / bucketCount

}
