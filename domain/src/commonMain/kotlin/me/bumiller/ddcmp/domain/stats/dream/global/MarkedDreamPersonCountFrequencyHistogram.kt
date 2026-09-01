package me.bumiller.ddcmp.domain.stats.dream.global

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import me.bumiller.ddcmp.domain.model.stats.MarkedHistogram
import me.bumiller.ddcmp.domain.model.stats.Stat
import me.bumiller.ddcmp.domain.model.stats.bucketFor
import me.bumiller.ddcmp.domain.model.stats.relativeRankFor
import me.bumiller.ddcmp.domain.stats.dream.global.MarkedDreamPersonCountFrequencyHistogram.I
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.meta.FlowUseCase
import kotlin.time.ExperimentalTime

class MarkedDreamPersonCountFrequencyHistogram(
    private val personCountFrequencyHistogram: DreamPersonCountFrequencyHistogram,
    private val allDreams: AllDreamUseCase
) : FlowUseCase<I, Stat<MarkedHistogram<Int, Unit, Int>>> {

    data class I(

        val range: ClosedRange<LocalDate>,

        val dreamId: Long

    ) {

        @OptIn(ExperimentalTime::class)
        fun dreamQuery() = AllDreamUseCase.I(id = dreamId)

    }

    override fun invoke(i: I): Flow<Stat<MarkedHistogram<Int, Unit, Int>>> {
        val lengthFreqHistogram = personCountFrequencyHistogram(i.range)

        val dream = allDreams(i.dreamQuery())
            .map { it.singleOrNull() }

        return dream.combine(lengthFreqHistogram) { dream, histogram ->
            val dream = dream ?: return@combine Stat.NotEnoughData()
            val histogram =
                (histogram as? Stat.Present)?.stat ?: return@combine Stat.NotEnoughData()

            val bucket = histogram.bucketFor(dream.persons.size)
            val rank = histogram.relativeRankFor(bucket)

            val markedHistogram = MarkedHistogram(
                histogram = histogram,
                markedBucket = bucket,
                rankPercentage = rank
            )
            Stat.Present(markedHistogram)
        }
    }

}