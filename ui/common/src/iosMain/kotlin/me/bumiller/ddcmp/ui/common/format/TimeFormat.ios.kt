package me.bumiller.ddcmp.ui.common.format

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