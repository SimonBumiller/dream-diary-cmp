package me.bumiller.ddcmp.ui.common.format

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaDayOfWeek
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

actual fun formatLocalDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    return formatter.format(date.toJavaLocalDate())
}

internal actual fun formatLocalDateMonth(date: LocalDate): String {
    return date.toJavaLocalDate()
        .format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()))
}

actual fun formatDayOfWeekShort(dayOfWeek: DayOfWeek): String {
    return dayOfWeek.toJavaDayOfWeek().getDisplayName(
        TextStyle.SHORT,
        Locale.getDefault()
    )
}

actual fun formatDayOfWeek(dayOfWeek: DayOfWeek): String {
    return dayOfWeek.toJavaDayOfWeek().getDisplayName(
        TextStyle.FULL,
        Locale.getDefault()
    )
}

actual fun formatLocalDateYearIndependent(date: LocalDate): String {
    return date.toJavaLocalDate()
        .format(DateTimeFormatter.ofPattern("dd MMMM", Locale.getDefault()))
}