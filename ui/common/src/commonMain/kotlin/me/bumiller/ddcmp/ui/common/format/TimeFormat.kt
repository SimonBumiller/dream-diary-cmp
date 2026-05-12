package me.bumiller.ddcmp.ui.common.format

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Formats a specific local date according to locale rules.
 */
internal expect fun formatLocalDate(date: LocalDate): String

@OptIn(ExperimentalTime::class)
fun formatInstant(instant: Instant): String {
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    return formatLocalDate(localDate)
}