package me.bumiller.ddcmp.domain.model.stats

/**
 * Models a histogram together with a selected bucket, and information about that bucket.
 */
data class MarkedHistogram<Bucket, BucketSize, Value : Number>(

    /**
     * THe histogram.
     */
    val histogram: Histogram<Bucket, BucketSize, Value>,

    /**
     * The marked bucket.
     */
    val markedBucket: Bucket,

    /**
     * The percentage rank of the bucket.
     */
    val rankPercentage: Float

) {

    val rank: HistogramRank
        get() = if (rankPercentage <= 0.15F) HistogramRank.Lowest
        else if (rankPercentage <= 0.4F) HistogramRank.Low
        else if (rankPercentage <= 0.6F) HistogramRank.Average
        else if (rankPercentage <= 0.85) HistogramRank.High
        else HistogramRank.Highest
}