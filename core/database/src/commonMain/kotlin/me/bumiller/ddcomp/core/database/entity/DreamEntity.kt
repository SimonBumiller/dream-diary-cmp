package me.bumiller.ddcomp.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import me.bumiller.ddcomp.core.database.entity.base.BaseEntity
import me.bumiller.ddcomp.core.database.entity.m2m.DreamEmotionM2M
import me.bumiller.ddcomp.core.database.entity.m2m.DreamPersonM2M
import me.bumiller.ddcomp.core.database.entity.m2m.DreamTagM2M
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Entity class for a dream.
 */
@OptIn(ExperimentalTime::class)
@Entity(
    tableName = "dreams"
)
data class DreamEntity(

    @PrimaryKey(autoGenerate = true)
    override val id: Long,

    override val created: Instant,

    override val updated: Instant,

    val title: String?,

    val dreamt: Instant,

    val description: String,

    val note: String?,

    val favorite: Boolean,

    val confidentiality: String

) : BaseEntity {

    data class Aggregate(

        @Embedded
        val dream: DreamEntity,

        @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = Junction(
                value = DreamPersonM2M::class,
                parentColumn = "dreamId",
                entityColumn = "personId"
            )
        )
        val persons: List<PersonEntity>,

        @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = Junction(
                value = DreamEmotionM2M::class,
                parentColumn = "dreamId",
                entityColumn = "emotionId"
            )
        )
        val emotions: List<EmotionEntity>,

        @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = Junction(
                value = DreamTagM2M::class,
                parentColumn = "dreamId",
                entityColumn = "tagId"
            )
        )
        val tags: List<TagEntity>

    )

}