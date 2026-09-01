package me.bumiller.ddcmp.domain.stats.dream.global.base

import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase

/**
 * Special case of a histogram statistic when only the dream frequency is counted, i.e. how many dreams are in a bucket.
 */
abstract class DreamFrequencyHistogramStatistic<I, Bucket, BucketSize>(
    getDreams: AllDreamUseCase,
    forceEnd: Boolean = false
) : DreamHistogramStatistic<I, Bucket, BucketSize, Int>(getDreams, forceEnd) {

    override fun getDreamValue(dream: Dream, bucket: Bucket): Int = 1

    override fun combineValues(values: List<Int>): Int = values.sum()

}