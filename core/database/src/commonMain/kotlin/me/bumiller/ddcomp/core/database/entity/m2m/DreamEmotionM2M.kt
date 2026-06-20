package me.bumiller.ddcomp.core.database.entity.m2m

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import me.bumiller.ddcomp.core.database.entity.DreamEntity
import me.bumiller.ddcomp.core.database.entity.EmotionEntity

@Entity(
    tableName = "dreams_emotions_m2m",
    primaryKeys = ["dreamId", "emotionId"],
    foreignKeys = [
        ForeignKey(
            entity = DreamEntity::class,
            parentColumns = ["id"],
            childColumns = ["dreamId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EmotionEntity::class,
            parentColumns = ["id"],
            childColumns = ["emotionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("dreamId"),
        Index("emotionId")
    ]
)
data class DreamEmotionM2M(

    val dreamId: Long,

    val emotionId: Long

)
