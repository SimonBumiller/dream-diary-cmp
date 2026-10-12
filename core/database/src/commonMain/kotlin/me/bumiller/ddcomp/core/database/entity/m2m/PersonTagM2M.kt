package me.bumiller.ddcomp.core.database.entity.m2m

import androidx.room.Entity
import androidx.room.ForeignKey
import me.bumiller.ddcomp.core.database.entity.DreamEntity
import me.bumiller.ddcomp.core.database.entity.PersonEntity
import me.bumiller.ddcomp.core.database.entity.TagEntity

@Entity(
    tableName = "persons_tags_m2m",
    primaryKeys = ["personId", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PersonTagM2M(

    val personId: Long,

    val tagId: Long

)
