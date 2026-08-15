package me.bumiller.ddcmp.domain.model

import kotlinx.datetime.LocalDate

private val LocalDateMin = LocalDate(-999_999_999, 1, 1)
private val LocalDateMax = LocalDate(999_999_999, 12, 31)

sealed class LocalDateRange: ClosedRange<LocalDate> {

    data class Normal(
        override val start: LocalDate,
        override val endInclusive: LocalDate
    ): LocalDateRange()

    data object Limitless: LocalDateRange() {
        override val endInclusive = LocalDateMax
        override val start = LocalDateMin
    }

    data class UpperBounded(
        override val endInclusive: LocalDate
    ): LocalDateRange() {
        override val start = LocalDateMin
    }

    data class LowerBounded(
        override val start: LocalDate
    ): LocalDateRange() {
        override val endInclusive = LocalDateMax
    }

}