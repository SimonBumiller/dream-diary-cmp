package me.bumiller.ddcomp.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import me.bumiller.ddcomp.core.database.entity.base.BaseEntity
import me.bumiller.ddcomp.core.database.entity.m2m.DreamTagM2M
import me.bumiller.ddcomp.core.database.entity.m2m.PersonTagM2M
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Entity class for a tag.
 */
@OptIn(ExperimentalTime::class)
@Entity(
    tableName = "tags"
)
data class TagEntity(

    @PrimaryKey(autoGenerate = true)
    override val id: Long,

    override val created: Instant,

    override val updated: Instant,

    val name: String,

    val description: String?,

    val color: Int

) : BaseEntity {

    data class Aggregate(

        @Embedded
        val tag: TagEntity,

        @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = Junction(
                value = DreamTagM2M::class,
                parentColumn = "tagId",
                entityColumn = "dreamId"
            )
        )
        val dreams: List<DreamEntity>,

        @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = Junction(
                value = PersonTagM2M::class,
                parentColumn = "tagId",
                entityColumn = "personId"
            )
        )
        val persons: List<PersonEntity>

    )
}