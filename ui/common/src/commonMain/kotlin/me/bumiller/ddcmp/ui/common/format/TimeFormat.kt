package me.bumiller.ddcmp.ui.common.format

import androidx.compose.runtime.IntState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Formats a specific local date according to locale rules.
 */
internal expect fun formatLocalDate(date: LocalDate): String

/**
 * Formats only the month and year part of a local date.
 */
internal expect fun formatLocalDateMonth(date: LocalDate): String

@OptIn(ExperimentalTime::class)
fun formatInstant(instant: Instant): String {
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    return formatLocalDate(localDate)
}

@OptIn(ExperimentalTime::class)
fun formatInstantMonthYear(instant: Instant): String {
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    return formatLocalDateMonth(localDate)
}