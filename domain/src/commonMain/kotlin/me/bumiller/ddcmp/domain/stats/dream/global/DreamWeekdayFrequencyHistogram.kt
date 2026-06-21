package me.bumiller.ddcmp.domain.stats.dream.global

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.model.stats.HistogramMeta
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import kotlin.time.ExperimentalTime

/**
 * A statistical usecase that calculates a histogram (day-of-the-week vs dream frequency) for a specified date range.
 */
@OptIn(ExperimentalTime::class)
class DreamWeekdayFrequencyHistogram(

    getDreams: AllDreamUseCase

) : DreamListBasedStatistic<ClosedRange<LocalDate>, HistogramMeta<DayOfWeek, Unit, Int>>(getDreams) {

    override fun ClosedRange<LocalDate>.invoke() = this

    override fun invoke(
        input: ClosedRange<LocalDate>,
        dreams: List<Dream>,
        actualRange: ClosedRange<LocalDate>
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

        if (meta.average == 0F)
            return null

        return meta
    }

}