package me.bumiller.ddcmp.domain.stats.dream.global

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.model.stats.Stat
import me.bumiller.ddcmp.domain.stats.dream.global.base.DreamListBasedStatistic
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import kotlin.time.ExperimentalTime

/**
 * Statistical use-case to calculate weekly averages over different timespans.
 */
@OptIn(ExperimentalTime::class)
class DreamWeeklyFrequencyStatistic(
    getDreams: AllDreamUseCase
) : DreamListBasedStatistic<DreamWeeklyFrequencyStatistic.I, DreamWeeklyFrequencyStatistic.WeeklyFrequency>(
    getDreams
) {

    override fun I.invoke(): ClosedRange<LocalDate> = dateRange

    override suspend fun invoke(
        input: I,
        dreams: List<Dream>,
        actualRange: ClosedRange<LocalDate>
    ): WeeklyFrequency? {
        if(input.referenceDay !in input.dateRange) return null

        val epochDays =
            dreams.map(Dream::dreamt)
                .map { instant -> instant.toLocalDateTime(timeZone).date.toEpochDays() }

        val firstEpochDay = epochDays.min()

        val thirtyDaysAgo = input.referenceDay - DatePeriod(days = 30)
        val sixMonthsAgo = input.referenceDay - DatePeriod(months = 6)

        val overall =
            calculateWeeklyAverage(epochDays, firstEpochDay, null, input.referenceDay.toEpochDays())
        val lastSevenDays = calculateLastSevenDays(epochDays, input.referenceDay.toEpochDays()).toFloat()
        val lastThirtyDays = calculateWeeklyAverage(
            epochDays,
            firstEpochDay,
            thirtyDaysAgo.toEpochDays(),
            input.referenceDay.toEpochDays()
        )
        val lastSixMonths = calculateWeeklyAverage(
            epochDays,
            firstEpochDay,
            sixMonthsAgo.toEpochDays(),
            input.referenceDay.toEpochDays()
        )

        val overallDaySpan = input.referenceDay.toEpochDays() - firstEpochDay

        return if(overallDaySpan < 7) null
        else WeeklyFrequency(
            overallAverage = overall!!,
            lastSevenDays = lastSevenDays,
            lastThirtyDays = Stat.ofNullable(lastThirtyDays),
            lastSixMonths = Stat.ofNullable(lastSixMonths)
        )
    }

    private fun calculateLastSevenDays(
        epochDays: List<Long>,
        referenceEpochDay: Long
    ): Int = epochDays.count { epochDay ->
        referenceEpochDay - epochDay <= 6
    }

    private fun calculateWeeklyAverage(
        epochDays: List<Long>,
        firstEpochDate: Long,
        rangeStartEpochDay: Long?,
        endEpochDay: Long
    ): Float? {
        if(rangeStartEpochDay != null && rangeStartEpochDay < firstEpochDate)
            return null

        // If we have no range set, we use the first dreams day as the start day.
        // If we have a range set, we use the first allowed day, i.e. the range start, as the first day
        val startEpochDay = rangeStartEpochDay ?: firstEpochDate

        val filtered = epochDays.filter { it in startEpochDay..endEpochDay }

        val startWeek = 0
        val endWeek = (endEpochDay - startEpochDay) / 7

        val dreamCounts = filtered // Epoch days
            .map { epochDay -> (epochDay - startEpochDay) / 7 } // Epoch weeks, starting at first dream epoch day
            .groupingBy { it }
            .eachCount()

        return (startWeek..endWeek)
            .map { week -> dreamCounts[week] ?: 0 }
            .average()
            .toFloat()
    }

    /**
     * The weekly frequency stat.
     */
    data class WeeklyFrequency(

        /**
         * The weekly average overall.
         */
        val overallAverage: Float,

        /**
         * Dream count last seven days.
         */
        val lastSevenDays: Float,

        /**
         * Weekly dream count over last thirty days
         */
        val lastThirtyDays: Stat<Float>,

        /**
         * Weekly dream count over last six months.
         */
        val lastSixMonths: Stat<Float>

    )

    /**
     * Config options.
     */
    data class I(

        /**
         * The date range.
         */
        val dateRange: ClosedRange<LocalDate>,

        /**
         * The current date.
         */
        val referenceDay: LocalDate

    )

}