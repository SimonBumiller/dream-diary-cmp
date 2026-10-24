package me.bumiller.ddcmp.common

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

fun Instant.toDosTime(): Long {
    val dt = toLocalDateTime(TimeZone.currentSystemDefault())

    val date = ((dt.year - 1980) shl 9) or
               (dt.month.number shl 5) or
            dt.day

    val time = (dt.hour shl 11) or
               (dt.minute shl 5) or
               (dt.second / 2)

    return ((date.toLong() shl 16) or time.toLong())
}