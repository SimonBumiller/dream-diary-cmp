package me.bumiller.ddcmp.domain.model.stats

/**
 * Meta-information about a numerical histogram.
 */
data class Histogram<Bucket, BucketSize, Value : Number>(

    /**
     * The actual histogram.
     */
    val dataMap: Map<Bucket, Value>,

    /**
     * The size of a single bucket.
     */
    val bucketSize: BucketSize,

    ) {

    /**
     * The number of buckets
     */
    val bucketCount: Int
        get() = dataMap.size

    /**
     * The average number per bucket.
     */
    val average: Float
        get() = dataMap.values.map { it.toFloat() }.sum() / bucketCount

}

/**
 * Calculates the bucket a value lies in.
 */
fun <T: Comparable<T>> Histogram<T, *, Int>.bucketFor(value: T): T {
    return dataMap
        .keys
        .sorted()
        .last { it <= value }
}