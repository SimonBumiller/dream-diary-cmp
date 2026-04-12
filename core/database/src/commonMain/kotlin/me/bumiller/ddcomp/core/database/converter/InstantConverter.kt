package me.bumiller.ddcomp.core.database.converter

import androidx.room.TypeConverter
import kotlin.time.Instant

class InstantConverter {

    @TypeConverter
    fun toLong(instant: Instant?) = instant?.epochSeconds

    @TypeConverter
    fun fromLong(long: Long?) = long?.let { Instant.fromEpochSeconds(long) }

}