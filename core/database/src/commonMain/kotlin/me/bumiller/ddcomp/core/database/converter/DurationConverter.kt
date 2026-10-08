package me.bumiller.ddcomp.core.database.converter

import androidx.room.TypeConverter
import kotlin.time.Duration
import kotlin.time.Instant

class DurationConverter {

    @TypeConverter
    fun toString(duration: Duration?): String? = duration?.toIsoString()

    @TypeConverter
    fun fromString(string: String?) = string?.let { Duration.parseIsoString(it) }

}