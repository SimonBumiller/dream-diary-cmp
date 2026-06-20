package me.bumiller.ddcomp.core.database.entity.m2m

import androidx.room.Entity
import androidx.room.ForeignKey
import me.bumiller.ddcomp.core.database.entity.DreamEntity
import me.bumiller.ddcomp.core.database.entity.PersonEntity

@Entity(
    tableName = "dreams_persons_m2m",
    primaryKeys = ["dreamId", "personId"],
    foreignKeys = [
        ForeignKey(
            entity = DreamEntity::class,
            parentColumns = ["id"],
            childColumns = ["dreamId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DreamPersonM2M(

    val dreamId: Long,

    val personId: Long

)
