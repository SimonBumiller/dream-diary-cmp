package me.bumiller.ddcomp.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.bumiller.ddcomp.core.database.entity.base.BaseEntity
import kotlin.time.Instant

@Entity(
    tableName = "attachment_metas"
)
data class AttachmentMetaEntity(

    @PrimaryKey(autoGenerate = true)
    override val id: Long,

    override val created: Instant,
    override val updated: Instant,

    val fileName: String,

    val mimeType: String,

    val description: String?,

    val name: String,

    val size: Long,

    val dreamId: Long

) : BaseEntity
