@file:OptIn(ExperimentalTime::class)

package me.bumiller.ddcmp.domain.stats.dream.global.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.model.meta.SortDirection
import me.bumiller.ddcmp.domain.model.meta.SortMode
import me.bumiller.ddcmp.domain.model.stats.Stat
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.meta.FlowUseCase
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Base class for statistics that take the dream list as a base.
 */
@OptIn(ExperimentalTime::class)
abstract class DreamListBasedStatistic<I, O>(

    /**
     * Usecase to get all dreams.
     */
    private val getDreams: AllDreamUseCase,

    /**
     * Whether the end value should be forced, or to be cut to the last dream-date.
     */
    private val forceEnd: Boolean = false

) : FlowUseCase<I, Stat<O>> {

    protected val timeZone = TimeZone.currentSystemDefault()

    override fun invoke(i: I): Flow<Stat<O>> = getDreams.asynchronousDreamStatistic(
        dateRange = i(),
        synchronousDelegate = { dreams ->
            val start = dreams.minOfOrNull { it.dreamt }?.toLocalDateTime(timeZone)?.date
            val end = if(forceEnd) i().endInclusive
                else dreams.maxOfOrNull { it.dreamt }?.toLocalDateTime(timeZone)?.date
            val empiricalRange = if (start != null && end != null) start..end
            else return@asynchronousDreamStatistic null

            this(i, dreams, empiricalRange)
        }
    )

    /**
     * Performs the processing on the dream list synchronously.
     *
     * @param input The delegated inputs
     * @param dreams The dreams from the specified date range
     * @param actualRange The empirical range of the dreams retrieved
     */
    protected abstract suspend operator fun invoke(
        input: I,
        dreams: List<Dream>,
        actualRange: ClosedRange<LocalDate>
    ): O?

    /**
     * Produces the date range from the input.
     */
    protected abstract operator fun I.invoke(): ClosedRange<LocalDate>

}

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
    dreamQuery: AllDreamUseCase.I = AllDreamUseCase.I(
        sortMode = SortMode.DreamSort.Dreamt,
        sortDirection = SortDirection.Ascending,
        timeRange = createInstantRange(dateRange)
    ),
    synchronousDelegate: suspend (List<Dream>) -> T?
): Flow<Stat<T>> {
    val dreams = this(dreamQuery).map { it.map(AllDreamUseCase.O::dream) }

    // We perform the calculation flow-independent.
    val returnValue = dreams.map(synchronousDelegate)

    return returnValue.map { Stat.ofNullable(it) }
}