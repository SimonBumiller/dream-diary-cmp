package me.bumiller.ddcmp.domain.stats.dream.global

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.stats.dream.global.DreamStreakStatistic.I
import me.bumiller.ddcmp.domain.stats.dream.global.DreamStreakStatistic.StreakStatistic
import me.bumiller.ddcmp.domain.stats.dream.global.base.DreamListBasedStatistic
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import kotlin.time.ExperimentalTime

/**
 * Statistical usecase that reports on the longest and current streak of dreams, i.e. consecutive days on which dreams have been entered.
 */
@OptIn(ExperimentalTime::class)
class DreamStreakStatistic(
    getDreams: AllDreamUseCase
) : DreamListBasedStatistic<I, StreakStatistic>(getDreams) {

    override fun I.invoke() = dateRange

    override fun invoke(
        input: I,
        dreams: List<Dream>,
        actualRange: ClosedRange<LocalDate>
    ): StreakStatistic? {
        val dates = dreams
            .asSequence()
            .map(Dream::dreamt)
            .map { it.toLocalDateTime(timeZone).date }
            .sorted()
            .distinct()
            .toList()

        val longestStreak = dates.longestConsecutive()
        val currentStreak =  if(input.referenceDay !in input.dateRange) emptyList()
            else dates.currentConsecutive(input.referenceDay)

        if (longestStreak.isEmpty())
            return null

        return StreakStatistic(longestStreak, currentStreak)
    }

    private fun List<LocalDate>.currentConsecutive(
        current: LocalDate
    ): List<LocalDate> {
        val currentIndex = indexOf(current)

        if (currentIndex == -1)
            return emptyList()

        var start = currentIndex
        var recentValue = current

        while (
            start - 1 in indices &&
            this[start - 1] == recentValue - DatePeriod(days = 1)
        ) {
            start--
            recentValue -= DatePeriod(days = 1)
        }

        return subList(start, currentIndex + 1)
    }

    private fun List<LocalDate>.longestConsecutive(): List<LocalDate> {
        if (isEmpty()) return emptyList()

        var bestStart = 0
        var bestLen = 1

        var curStart = 0
        var curLen = 1

        for (i in 1 until size) {
            if (this[i] == this[i - 1] + DatePeriod(days = 1)) {
                curLen++
            } else {
                curStart = i
                curLen = 1
            }

            if (curLen > bestLen) {
                bestLen = curLen
                bestStart = curStart
            }
        }

        return subList(bestStart, bestStart + bestLen)
    }

    /**
     * Streak statistic data
     */
    data class StreakStatistic(

        /**
         * The longest streak.
         */
        val longest: List<LocalDate>,

        /**
         * The current streak.
         */
        val current: List<LocalDate>

    )

    /**
     * The input.
     */
    data class I(

        /**
         * The date range for the dreams.
         */
        val dateRange: ClosedRange<LocalDate>,

        /**
         * The reference day for the current streak.
         * I.e. a streak is current if the reference day is contained.
         */
        val referenceDay: LocalDate

    )

}