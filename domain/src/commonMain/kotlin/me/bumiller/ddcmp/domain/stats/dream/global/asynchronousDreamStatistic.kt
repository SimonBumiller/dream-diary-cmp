@file:OptIn(ExperimentalTime::class)

package me.bumiller.ddcmp.domain.stats.dream.global

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.model.meta.SortDirection
import me.bumiller.ddcmp.domain.model.meta.SortMode
import me.bumiller.ddcmp.domain.model.stats.Stat
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase.I
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase.O
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private val timeZone = TimeZone.currentSystemDefault()

private fun createInstantRange(dateRange: ClosedRange<LocalDate>): ClosedRange<Instant> {
    val start = dateRange.start.atStartOfDayIn(timeZone)
    val end = dateRange.endInclusive.atStartOfDayIn(timeZone)

    return start..end
}

/**
 * Wrapper to perform synchronous stat-calculations on dreams.
 */
fun <T> AllDreamUseCase.asynchronousDreamStatistic(
    dateRange: ClosedRange<LocalDate>,
    dreamQuery: AllDreamUseCase.I = I(
        sortMode = SortMode.DreamSort.Dreamt,
        sortDirection = SortDirection.Ascending,
        timeRange = createInstantRange(dateRange)
    ),
    synchronousDelegate: (List<Dream>) -> T?
): Flow<Stat<T>> {
    val dreams = this(dreamQuery).map { it.map(O::dream) }

    // We perform the calculation flow-independent.
    val returnValue = dreams.map(synchronousDelegate)

    return returnValue.map { Stat.ofNullable(it) }
}