package me.bumiller.ddcomp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.bumiller.ddcomp.core.database.entity.base.BaseEntity
import kotlin.time.Instant

@Entity(
    tableName = "backup_destinations"
)
data class BackupDestinationEntity(

    @PrimaryKey(autoGenerate = true)
    override val id: Long,

    override val created: Instant,

    override val updated: Instant,

    val localAndroidPath: String?,

    val localAndroidUri: String?,

    val localIosUri: String?,

    val localJvmPath: String?

): BaseEntity
