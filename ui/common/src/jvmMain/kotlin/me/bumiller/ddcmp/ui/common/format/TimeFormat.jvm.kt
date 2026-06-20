package me.bumiller.ddcmp.ui.common.format

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

internal actual fun formatLocalDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    return formatter.format(date.toJavaLocalDate())
}

internal actual fun formatLocalDateMonth(date: LocalDate): String {
    return date.toJavaLocalDate()
        .format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()))
}