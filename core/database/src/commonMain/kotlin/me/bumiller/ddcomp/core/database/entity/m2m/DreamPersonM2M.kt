package me.bumiller.ddcomp.core.database.entity.m2m

import androidx.room.Entity

@Entity(
    tableName = "dreams_persons_m2m",
    primaryKeys = ["dreamId", "personId"]
)
data class DreamPersonM2M(

    val dreamId: Long,

    val personId: Long

)
