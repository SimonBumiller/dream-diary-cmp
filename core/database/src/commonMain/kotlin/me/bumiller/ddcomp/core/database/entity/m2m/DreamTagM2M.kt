package me.bumiller.ddcomp.core.database.entity.m2m

import androidx.room.Entity
import androidx.room.ForeignKey
import me.bumiller.ddcomp.core.database.entity.DreamEntity
import me.bumiller.ddcomp.core.database.entity.PersonEntity
import me.bumiller.ddcomp.core.database.entity.TagEntity

@Entity(
    tableName = "dreams_tags_m2m",
    primaryKeys = ["dreamId", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = DreamEntity::class,
            parentColumns = ["id"],
            childColumns = ["dreamId"],
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
data class DreamTagM2M(

    val dreamId: Long,

    val tagId: Long

)
