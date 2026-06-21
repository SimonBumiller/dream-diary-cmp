package me.bumiller.ddcmp.domain.stats.dream.global

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.model.stats.HistogramMeta
import me.bumiller.ddcmp.domain.model.stats.Stat
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.meta.FlowUseCase
import kotlin.time.ExperimentalTime

/**
 * A statistical usecase that calculates a histogram (day-of-the-week vs dream frequency) for a specified date range.
 */
@OptIn(ExperimentalTime::class)
class DreamWeekdayFrequencyHistogram(

    /**
     * Usecase to get all available dreams.
     */
    private val getDreams: AllDreamUseCase

) : FlowUseCase<ClosedRange<LocalDate>, Stat<HistogramMeta<DayOfWeek, Unit, Int>>> {

    private val timeZone = TimeZone.currentSystemDefault()

    override fun invoke(i: ClosedRange<LocalDate>): Flow<Stat<HistogramMeta<DayOfWeek, Unit, Int>>>
        = getDreams.asynchronousDreamStatistic(
            dateRange = i,
            synchronousDelegate = ::delegateAsynchronous
        )

    private fun delegateAsynchronous(
        dreams: List<Dream>
    ): HistogramMeta<DayOfWeek, Unit, Int>? {
        val histogram = dreams
            .groupBy { dream ->
                dream
                    .dreamt
                    .toLocalDateTime(timeZone)
                    .dayOfWeek
            }
            .mapValues { entry -> entry.value.size }

        val meta = HistogramMeta(
            histogram = histogram,
            bucketSize = Unit,
            bucketCount = DayOfWeek.entries.size
        )

        if(meta.average == 0F)
            return null

        return meta
    }


}