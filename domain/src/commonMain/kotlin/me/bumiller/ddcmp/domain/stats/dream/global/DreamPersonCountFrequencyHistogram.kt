package me.bumiller.ddcmp.domain.stats.dream.global

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import me.bumiller.ddcmp.domain.infrastructure.PersonRepository
import me.bumiller.ddcmp.domain.stats.dream.global.base.DreamFrequencyHistogramStatistic
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import kotlin.time.ExperimentalTime

/**
 * A statistical usecase that calculates a histogram (word count in dream vs dream frequency) for a specified date range.
 */
@OptIn(ExperimentalTime::class)
class DreamPersonCountFrequencyHistogram(
    private val personRepo: PersonRepository,
    getDreams: AllDreamUseCase
) : DreamFrequencyHistogramStatistic<ClosedRange<LocalDate>, Int, Unit>(getDreams) {

    override fun ClosedRange<LocalDate>.invoke() = this

    override suspend fun createMetadata(
        input: ClosedRange<LocalDate>,
        actualRange: ClosedRange<LocalDate>,
        dreams: List<AllDreamUseCase.O>
    ): Pair<List<Int>, Unit> {
        val personCounts = dreams.map {
            personRepo.getForDream(it.dream.id)
                .map { persons -> persons?.size ?: 0 }
                .first()
        }

        val lowest = personCounts.max()
        val highest = personCounts.min()

        val range = highest..lowest

        return range.toList() to Unit
    }

    override suspend fun findBucketFor(
        dream: AllDreamUseCase.O,
        buckets: List<Int>,
        input: ClosedRange<LocalDate>
    ): Int = personRepo
        .getForDream(dream.dream.id)
        .first()
        ?.size ?: 0

}