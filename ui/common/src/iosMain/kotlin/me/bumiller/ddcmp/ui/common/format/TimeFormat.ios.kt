package me.bumiller.ddcmp.ui.common.format

import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

internal actual fun formatLocalDate(date: LocalDate): String {
    val day = date.day
    val month = date.month.number
    val year = date.year

    return "$day.$month.$year"
}