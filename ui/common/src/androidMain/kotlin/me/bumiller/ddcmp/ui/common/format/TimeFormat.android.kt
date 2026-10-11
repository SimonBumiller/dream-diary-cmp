package me.bumiller.ddcmp.ui.common.format

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaDayOfWeek
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale
import kotlin.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
actual fun formatLocalDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    return formatter.format(date.toJavaLocalDate())
}

internal actual fun formatLocalDateMonth(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
    return date.toJavaLocalDate().format(formatter)
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
    val formatter = DateTimeFormatter.ofPattern("dd MMMM", Locale.getDefault())
    return date.toJavaLocalDate().format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
actual fun formatLocalDateTime(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    return formatter.format(dateTime.toJavaLocalDateTime())
}