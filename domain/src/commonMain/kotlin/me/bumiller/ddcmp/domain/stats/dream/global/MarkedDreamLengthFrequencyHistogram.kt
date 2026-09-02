package me.bumiller.ddcmp.domain.stats.dream.global

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import me.bumiller.ddcmp.domain.model.stats.Histogram
import me.bumiller.ddcmp.domain.model.stats.MarkedHistogram
import me.bumiller.ddcmp.domain.model.stats.Stat
import me.bumiller.ddcmp.domain.model.stats.bucketFor
import me.bumiller.ddcmp.domain.stats.dream.global.MarkedDreamLengthFrequencyHistogram.I
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.meta.FlowUseCase
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class MarkedDreamLengthFrequencyHistogram(
    private val lengthFrequencyHistogram: DreamLengthFrequencyHistogram,
    private val allDreams: AllDreamUseCase
) : FlowUseCase<I, Stat<MarkedHistogram<Int, Int, Int>>> {

    data class I(

        val range: ClosedRange<LocalDate>,

        val dreamId: Long

    )

    private val timeZone = TimeZone.currentSystemDefault()

    private fun singleDreamQuery(id: Long) = AllDreamUseCase.I(id = id)
    private fun allDreamsQuery(range: ClosedRange<LocalDate>): AllDreamUseCase.I {
        val start = range.start.atStartOfDayIn(timeZone)
        val end = range.endInclusive.plus(DatePeriod(days = 1)).atStartOfDayIn(timeZone)
        return AllDreamUseCase.I(
            timeRange = start..end
        )
    }

    override fun invoke(i: I): Flow<Stat<MarkedHistogram<Int, Int, Int>>> {
        val lengthFreqHistogram = lengthFrequencyHistogram(i.range)

        val dream = allDreams(singleDreamQuery(i.dreamId))
            .map { it.singleOrNull() }

        val dreams = allDreams(allDreamsQuery(i.range))

        return combine(
            flow = dream,
            flow2 = dreams,
            flow3 = lengthFreqHistogram
        ) { dream, dreams, histogram ->
            if (dream == null ||
                (histogram as? Stat.Present)?.stat == null
            ) Stat.NotEnoughData<MarkedHistogram<Int, Int, Int>>()

            createMarkedHistogram(
                dream = dream!!,
                dreams = dreams,
                histogram = (histogram as Stat.Present).stat
            ).let { Stat.Present(it) }
        }
    }

    private fun createMarkedHistogram(
        dream: AllDreamUseCase.O,
        dreams: List<AllDreamUseCase.O>,
        histogram: Histogram<Int, Int, Int>
    ): MarkedHistogram<Int, Int, Int> {
        val selectedBucket = histogram.bucketFor(dream.dream.length)

        val dreamsShorter = dreams.count { it.dream.length < dream.dream.length }
        val rank = dreamsShorter.toFloat() / dreams.size

        return MarkedHistogram(
            histogram = histogram,
            markedBucket = selectedBucket,
            rankPercentage = rank
        )
    }

}