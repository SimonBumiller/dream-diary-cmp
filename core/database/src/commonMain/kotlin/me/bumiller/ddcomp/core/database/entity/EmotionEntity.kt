package me.bumiller.ddcomp.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import me.bumiller.ddcomp.core.database.entity.base.BaseEntity
import me.bumiller.ddcomp.core.database.entity.m2m.DreamEmotionM2M
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Entity class for an emotion.
 */
@OptIn(ExperimentalTime::class)
@Entity(
    tableName = "emotions"
)
data class EmotionEntity (

    @PrimaryKey(autoGenerate = true)
    override val id: Long,

    override val created: Instant,

    override val updated: Instant,

    val arousal: Float,

    val valence: Float,

    val name: String

) : BaseEntity {

    data class Aggregate(

        @Embedded
        val emotion: EmotionEntity,

        @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = Junction(
                value = DreamEmotionM2M::class,
                parentColumn = "emotionId",
                entityColumn = "dreamId"
            )
        )
        val dreams: List<DreamEntity>

    )

}