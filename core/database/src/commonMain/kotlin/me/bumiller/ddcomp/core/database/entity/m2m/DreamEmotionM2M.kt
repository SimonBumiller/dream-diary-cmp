package me.bumiller.ddcomp.core.database.entity.m2m

import androidx.room.Entity

@Entity(
    tableName = "dreams_emotions_m2m",
    primaryKeys = ["dreamId", "emotionId"]
)
data class DreamEmotionM2M(

    val dreamId: Long,

    val emotionId: Long

)
