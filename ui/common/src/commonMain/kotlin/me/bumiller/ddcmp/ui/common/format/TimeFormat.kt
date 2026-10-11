package me.bumiller.ddcmp.ui.common.format

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Formats a specific local date according to locale rules.
 */
expect fun formatLocalDate(date: LocalDate): String

/**
 * Formats a specific local date-time according to locale rules.
 */
expect fun formatLocalDateTime(dateTime: LocalDateTime): String

/**
 * Formats only the month and year part of a local date.
 */
internal expect fun formatLocalDateMonth(date: LocalDate): String

expect fun formatLocalDateYearIndependent(date: LocalDate): String

@OptIn(ExperimentalTime::class)
fun formatInstant(instant: Instant): String {
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    return formatLocalDate(localDate)
}

fun formatInstantDayTime(instant: Instant): String {
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    return formatLocalDateTime(dateTime)
}

/**
 * Get the abbreviated version of the name of the day-of-week
 */
expect fun formatDayOfWeekShort(dayOfWeek: DayOfWeek): String

/**
 * Get the version of the name of the day-of-week
 */
expect fun formatDayOfWeek(dayOfWeek: DayOfWeek): String

@OptIn(ExperimentalTime::class)
fun formatInstantMonthYear(instant: Instant): String {
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    return formatLocalDateMonth(localDate)
}