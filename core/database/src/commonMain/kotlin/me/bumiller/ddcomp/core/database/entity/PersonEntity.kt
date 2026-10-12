package me.bumiller.ddcomp.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import me.bumiller.ddcomp.core.database.entity.base.BaseEntity
import me.bumiller.ddcomp.core.database.entity.m2m.DreamPersonM2M
import me.bumiller.ddcomp.core.database.entity.m2m.PersonTagM2M
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Entity class for a person.
 */
@OptIn(ExperimentalTime::class)
@Entity(
    tableName = "persons"
)
data class PersonEntity (

    @PrimaryKey(autoGenerate = true)
    override val id: Long,

    override val created: Instant,

    override val updated: Instant,

    val name: String,

    val note: String?

) : BaseEntity {

    data class Aggregate(

        @Embedded
        val person: PersonEntity,

        @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = Junction(
                value = DreamPersonM2M::class,
                parentColumn = "personId",
                entityColumn = "dreamId"
            )
        )
        val dreams: List<DreamEntity>,

        @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = Junction(
                value = PersonTagM2M::class,
                parentColumn = "personId",
                entityColumn = "tagId"
            )
        )
        val tags: List<TagEntity>

    )
}