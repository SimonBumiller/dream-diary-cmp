package me.bumiller.ddcmp.ui.common.format

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

internal actual fun formatLocalDate(date: LocalDate): String {
    val day = date.day
    val month = date.month.number
    val year = date.year

    return "$day.$month.$year"
}

internal actual fun formatLocalDateMonth(date: LocalDate): String {
    val components = NSCalendar.currentCalendar.components(
        NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
        fromDate = NSDate()
    )

    components.year = date.year.toLong()
    components.month = date.month.number.toLong()
    components.day = date.day.toLong()

    val date = NSCalendar.currentCalendar.dateFromComponents(components)!!

    val formatter = NSDateFormatter().apply {
        dateFormat = "MMMM yyyy"
        locale = NSLocale.currentLocale
    }

    return formatter.stringFromDate(date)
}

actual fun formatDayOfWeekShort(dayOfWeek: DayOfWeek): String {
    val formatter = NSDateFormatter()
    val symbols = formatter.shortWeekdaySymbols

    val index = when (dayOfWeek) {
        DayOfWeek.SUNDAY -> 0
        DayOfWeek.MONDAY -> 1
        DayOfWeek.TUESDAY -> 2
        DayOfWeek.WEDNESDAY -> 3
        DayOfWeek.THURSDAY -> 4
        DayOfWeek.FRIDAY -> 5
        DayOfWeek.SATURDAY -> 6
    }

    return symbols[index] as String
}

actual fun formatLocalDateYearIndependent(date: LocalDate): String {
    val components = NSCalendar.currentCalendar.components(
        NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
        fromDate = NSDate()
    )

    components.year = date.year.toLong()
    components.month = date.month.number.toLong()
    components.day = date.day.toLong()

    val date = NSCalendar.currentCalendar.dateFromComponents(components)!!

    val formatter = NSDateFormatter().apply {
        dateFormat = "dd MMMM"
        locale = NSLocale.currentLocale
    }

    return formatter.stringFromDate(date)
}