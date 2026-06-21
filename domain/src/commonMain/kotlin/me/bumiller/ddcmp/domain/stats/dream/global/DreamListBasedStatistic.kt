package me.bumiller.ddcmp.domain.stats.dream.global

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.bumiller.ddcmp.domain.model.Dream
import me.bumiller.ddcmp.domain.model.stats.Stat
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.meta.FlowUseCase
import kotlin.time.ExperimentalTime

/**
 * Base class for statistics that take the dream list as a base.
 */
@OptIn(ExperimentalTime::class)
abstract class DreamListBasedStatistic<I, O>(

    /**
     * Usecase to get all dreams.
     */
    private val getDreams: AllDreamUseCase

): FlowUseCase<I, Stat<O>> {

    protected val timeZone = TimeZone.currentSystemDefault()

    override fun invoke(i: I): Flow<Stat<O>> = getDreams.asynchronousDreamStatistic(
        dateRange = i(),
        synchronousDelegate = { dreams ->
            val start = dreams.minOf { it.dreamt }.toLocalDateTime(timeZone).date
            val end = dreams.maxOf { it.dreamt }.toLocalDateTime(timeZone).date
            val empiricalRange = start..end

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
    protected abstract operator fun invoke(input: I, dreams: List<Dream>, actualRange: ClosedRange<LocalDate>): O?

    /**
     * Produces the date range from the input.
     */
    protected abstract operator fun I.invoke(): ClosedRange<LocalDate>

}